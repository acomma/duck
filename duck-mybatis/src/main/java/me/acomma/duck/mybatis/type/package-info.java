/**
 * 数据库无关的 JSON 类型处理器。
 *
 * <p>本包只放不依赖具体数据库的实现和全局配置：
 * <ul>
 * <li>{@link BaseJsonTypeHandler}：序列化/读取和默认文本绑定的基类；需要其他写入方式时覆盖 {@code setParameter} / {@code setNonNullParameter}；</li>
 * <li>{@link JsonTypeHandler}：使用默认文本绑定的具体处理器；</li>
 * <li>{@link ObjectMapperHolder}：JSON 处理器共用的全局 {@link com.fasterxml.jackson.databind.ObjectMapper} 配置。</li>
 * </ul>
 *
 * <p>数据库厂商专用的绑定放在 {@code me.acomma.duck.mybatis.type.<vendor>} 子包，例如 PostgreSQL 原生的 {@code json}/{@code jsonb} 处理器在 {@code me.acomma.duck.mybatis.type.postgres}。
 * 以后需要 MySQL 专属实现时再建 {@code type.mysql}，不要把厂商类型混进本包。
 *
 * <h2>与 type.postgres 的同名类</h2>
 * <p>两个包都使用短类名：本包的 {@link BaseJsonTypeHandler} 是数据库无关的基类（默认文本绑定），{@link JsonTypeHandler} 是基于它的文本处理器；
 * {@code type.postgres} 的 {@link me.acomma.duck.mybatis.type.postgres.BaseJsonTypeHandler} / {@link me.acomma.duck.mybatis.type.postgres.JsonTypeHandler} / {@link me.acomma.duck.mybatis.type.postgres.JsonbTypeHandler} 处理 PostgreSQL 原生 {@code json}/{@code jsonb}。
 * 简单名相同，靠包名区分；同一个文件同时使用两者时，其中一个需要写全限定名。
 *
 * <h2>一个处理器覆盖所有目标类型</h2>
 * <p>MyBatis 3.6.0 的 {@code TypeHandlerRegistry} 在实例化处理器时优先查找 {@code (java.lang.reflect.Type)} 构造器（见 {@code TypeHandlerRegistry#getInstance}），并按<em>属性或参数的完整泛型类型</em>调用它：
 * <ul>
 * <li>{@code @Result} / XML {@code <result>}：{@code MapperBuilderAssistant#resolveSetterType} 用 {@code MetaClass#getGenericSetterType} 解析 Bean 的 setter 泛型；</li>
 * <li>{@code #{...}} / {@code ParameterMapping}：{@code ParameterMappingTokenHandler#figureOutJavaType} 解析属性泛型或 {@code @Param} 的类型；</li>
 * <li>自动映射：{@code DefaultResultSetHandler} 同样按 {@code MetaObject#getGenericSetterType} 取泛型类型。</li>
 * </ul>
 * <p>因此 {@link JsonTypeHandler} 只按构造器拿到的 {@link java.lang.reflect.Type} 构造 Jackson {@code JavaType}，Bean、{@code List<Foo>}、{@code Map<String, Object>}、{@code List<Foo>[]} 都用同一个类，不需要为每种元素类型准备固定类型（{@code TypeReference}）的子类。
 * 这也是放弃 {@code com.fasterxml.jackson.core.type.TypeReference} 的原因：泛型信息由 MyBatis 提供，处理器不再需要自己的类型令牌。
 *
 * <h2>适用场景</h2>
 * <ul>
 * <li>PostgreSQL 的 {@code text}、{@code varchar} 列：数据库不会校验 JSON，读写都按普通字符串处理。PostgreSQL 原生的 {@code json}/{@code jsonb} 列请使用 {@link me.acomma.duck.mybatis.type.postgres.JsonTypeHandler} / {@link me.acomma.duck.mybatis.type.postgres.JsonbTypeHandler}。</li>
 * <li>MySQL 5.7.8+ 的 {@code JSON} 列：MySQL 在写入时按 JSON 解析并校验；MySQL 没有 {@code jsonb} 类型，{@code JSON} 就是它的原生 JSON 类型。</li>
 * <li>MariaDB 的 {@code JSON}：本质是带 {@code json_valid} 约束的 {@code LONGTEXT}，同样按字符串绑定。</li>
 * <li>其他以 {@code varchar}/{@code text} 等字符串列保存 JSON 的数据库。</li>
 * </ul>
 *
 * <p>本包的 {@link JsonTypeHandler} 不依赖任何数据库厂商的 JDBC 扩展类型，因此没有 PostgreSQL JDBC 驱动时也可以使用；写入统一调用 {@link java.sql.PreparedStatement#setString(int, String)}，SQL NULL 统一按 {@link java.sql.Types#VARCHAR} 绑定。
 *
 * <h2>基本用法</h2>
 * <p>处理器由 MyBatis 反射实例化，只能在 {@code @Results} 或 XML 中显式指定。MyBatis 会按属性类型调用 {@code (Class)} 或 {@code (Type)} 构造器：
 * <pre>{@code
 * public class Foo {
 *     private Payload payload;             // JSON object -> 具体类
 *     private List<Foo> children;          // JSON array  -> List<Foo>
 *     private Map<String, Object> extra;   // 动态 object -> Map<String, Object>
 *     private List<Foo>[] matrix;          // 嵌套数组    -> List<Foo>[]
 * }
 *
 * @Results(id = "foo", value = {
 *         @Result(column = "payload", property = "payload", typeHandler = JsonTypeHandler.class),
 *         @Result(column = "children", property = "children", typeHandler = JsonTypeHandler.class),
 *         @Result(column = "extra", property = "extra", typeHandler = JsonTypeHandler.class),
 *         @Result(column = "matrix", property = "matrix", typeHandler = JsonTypeHandler.class),
 * })
 * }</pre>
 *
 * <p>PostgreSQL 文本列与 MySQL JSON 列使用同一个处理器，不需要按数据库区分：
 * <pre>{@code
 * @Insert("insert into foo(children) values (#{children, typeHandler = me.acomma.duck.mybatis.type.JsonTypeHandler})")
 * }</pre>
 *
 * <h2>目标类型</h2>
 * <p>处理器的目标类型就是构造器收到的 {@link java.lang.reflect.Type}：
 * <ul>
 * <li>{@link Class}：普通 Bean、标量，以及没有类型参数的集合/Map 子类（如 {@code class Tags extends ArrayList<String>}）；</li>
 * <li>{@link java.lang.reflect.ParameterizedType}、{@link java.lang.reflect.GenericArrayType}：集合、{@code Map}、{@code Iterable}、{@code Optional}、{@code Map.Entry}、自定义泛型容器/泛型 Bean，以及它们的数组（如 {@code List<Foo>[]}）。</li>
 * </ul>
 *
 * <p>用原始 {@code Class} 描述带类型参数的类仍然会被 {@link BaseJsonTypeHandler#requireResolvableType(java.lang.reflect.Type)} 拒绝：{@code List.class} 在运行期不携带元素类型，静默按 {@code List<Object>} 处理只会把错误推迟到读取阶段。
 * 正常写法不会碰到这个限制——MyBatis 传的就是完整泛型类型；只有手工构造处理器（{@code new JsonTypeHandler<>(List.class)}）或显式写了 {@code javaType = List.class} 时才会触发。
 * {@link Class} 自身带类型参数，但它是合法的标量目标，单独放行。
 *
 * <p>写入方向不依赖目标类型：Jackson 的 {@code writeValueAsString(Object)} 按运行期类型序列化，所以即使 MyBatis 只能解析出 {@code Object}（例如单个未命名的 {@code List} 参数写作 {@code #{list}}），序列化结果依然正确。
 *
 * <h2>容器建模</h2>
 * <p>JSON 只有 object 和 array 两种复合结构，Java 侧选什么容器是建模问题：
 * <ul>
 * <li><b>JSON object</b>：已知字段优先定义具体类；只有 key 不固定（metadata、extensions、headers 等）才用 {@code Map}。{@code Map<String,String>} 这类已知键值也优先具体类。</li>
 * <li><b>JSON array</b>：优先 {@code List}；它和 JSON 数组一一对应。{@code Set} 不是 JSON 原生类型，普通 {@code Set}（如 {@code HashSet}）序列化成数组后没有顺序保证，反序列化还会静默丢弃重复元素；唯一性应由领域层校验。</li>
 * </ul>
 * <p>需要 {@code Map}/{@code Set} 时直接把字段声明成对应的泛型类型即可，不需要自定义处理器：
 * <pre>{@code
 * private Map<String, Object> metadata;   // typeHandler = JsonTypeHandler.class
 * private Set<Foo> tags;                  // typeHandler = JsonTypeHandler.class
 * }</pre>
 * <p>需要保留任意 JSON 树（嵌套数组、数字精度）时，字段直接用 {@code JsonNode}：它不是 Collection/Map，按标量目标处理。
 * 使用 {@code Set} 时实体字段最好声明为 {@code LinkedHashSet}/{@code TreeSet}（后者要求元素可比较），顺序由 Jackson 按声明的具体集合类型决定；声明成接口 {@code Set} 时 Jackson 通常返回 {@code LinkedHashSet}，不保证顺序，元素还需要正确的 {@code equals}/{@code hashCode}，否则去重结果不可靠。
 *
 * <h2>全局 ObjectMapper</h2>
 * <p>未显式传入 {@link com.fasterxml.jackson.databind.ObjectMapper} 的构造器在构造时取 {@link ObjectMapperHolder#getDefault()}。
 * 应用应在 MyBatis 解析 mapper、构造处理器之前调用一次 {@link ObjectMapperHolder#setDefault(com.fasterxml.jackson.databind.ObjectMapper)} 替换默认实现：Spring Boot 放在 {@code ConfigurationCustomizer}，普通 MyBatis 放在 {@code SqlSessionFactoryBuilder#build} 之前。
 * 设置后只影响新构造的处理器，已构造的处理器继续使用自己的 mapper。需要给单个字段或不同数据源使用不同 mapper 时，写一个在构造器里调用 {@code super(mapper, type)} 的处理器子类。
 * {@code type.postgres} 的处理器共用同一套规则。
 *
 * <h2>限制</h2>
 * <ul>
 * <li>PostgreSQL 的 {@code json}/{@code jsonb} 列不能用本包的 {@link JsonTypeHandler}：PostgreSQL 不会把 {@code varchar} 参数隐式转换成 {@code jsonb}，需要 {@code PGobject} 或显式 SQL 转换，请使用 {@code type.postgres} 包中的处理器。</li>
 * <li>MySQL 只有 {@code JSON}，没有 {@code jsonb}；MariaDB 的 {@code JSON} 实际是 {@code LONGTEXT} 别名。</li>
 * <li>{@code text}/{@code varchar} 列不会校验 JSON 合法性，写入非法 JSON 要等到读取反序列化时才会报错；MySQL 的 {@code JSON} 列会在写入时校验。</li>
 * <li>Java 类型为 {@code String} 时，字符串会按 JSON 值序列化并带上引号。字段本身已是 JSON 文本时请直接用 {@code StringTypeHandler}，不要套用本处理器。</li>
 * <li>不要通过 {@code mybatis.type-handlers-package} 扫描本包。{@code TypeHandlerRegistry#register(Class)} 用 {@code TypeParameterResolver#resolveClassTypeParams} 推导注册键，而 {@link JsonTypeHandler} 自身的类型参数无法解析，会被当成 {@code Object} 注册成“智能处理器”；此后任何落到 {@code Object} 或原始类型的查找都会实例化它，把普通的 {@code Map}/{@code List} 属性变成 JSON 处理器，甚至因原始类型被拒而报错。本包的处理器只在 {@code @Results} / XML / {@code #{...}} 中显式指定。</li>
 * <li>自动映射（不写 {@code @Result}，靠列名与属性名对应）默认找不到本包的处理器：注册表按精确类型查找，没有“任意泛型”的通配处理器。确需自动映射时，在构造 {@code SqlSessionFactory} 前显式注册目标类型：
 * <pre>{@code
 * configuration.getTypeHandlerRegistry()
 *         .register(new org.apache.ibatis.type.TypeReference<List<Foo>>() {}.getRawType(), JsonTypeHandler.class);
 * }</pre>
 * 注册表会记住 {@code (Type)} 构造器，遇到同一个 {@code List<Foo>} 时按该类型实例化。元素类型不同就要分别注册；这也是本包不预置“元素类型 × 数据库”组合处理器的原因。</li>
 * </ul>
 */
package me.acomma.duck.mybatis.type;

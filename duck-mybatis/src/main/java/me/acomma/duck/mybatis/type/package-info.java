/**
 * 数据库无关的 JSON 类型处理器。
 *
 * <p>本包只放不依赖具体数据库的实现和全局配置：
 * <ul>
 * <li>{@link BaseJsonTypeHandler}：序列化/读取和默认文本绑定的基类；需要其他写入方式时覆盖 {@code setParameter} / {@code setNonNullParameter}；</li>
 * <li>{@link JsonTypeHandler}：使用默认文本绑定的具体处理器；自定义泛型类型继承它并用 {@link com.fasterxml.jackson.core.type.TypeReference} 固定完整类型；</li>
 * <li>{@code <元素类型>ListJsonTypeHandler}：文本绑定的预置集合处理器，覆盖常用元素类型；</li>
 * <li>{@link ObjectMapperHolder}：JSON 处理器共用的全局 {@link com.fasterxml.jackson.databind.ObjectMapper} 配置。</li>
 * </ul>
 *
 * <p>数据库厂商专用的绑定放在 {@code me.acomma.duck.mybatis.type.<vendor>} 子包，例如 PostgreSQL 原生的 {@code json}/{@code jsonb} 处理器在 {@code me.acomma.duck.mybatis.type.postgres}。
 * 以后需要 MySQL 专属实现时再建 {@code type.mysql}，不要把厂商类型混进本包。
 *
 * <h2>与 type.postgres 的同名类</h2>
 * <p>两个包都使用短类名：本包的 {@link BaseJsonTypeHandler} 是数据库无关的基类（默认文本绑定），{@link JsonTypeHandler} 是基于它的文本处理器；
 * {@code type.postgres} 的 {@link me.acomma.duck.mybatis.type.postgres.BaseJsonTypeHandler} / {@link me.acomma.duck.mybatis.type.postgres.JsonTypeHandler} / {@link me.acomma.duck.mybatis.type.postgres.JsonbTypeHandler} 处理 PostgreSQL 原生 {@code json}/{@code jsonb}。
 * 两个包还各有一套 {@code <元素类型>ListJsonTypeHandler}：本包是文本绑定，{@code type.postgres} 的同名类是 PostgreSQL 原生 {@code json} 绑定，{@code <元素类型>ListJsonbTypeHandler} 是 {@code jsonb} 绑定。
 * 简单名相同，靠包名区分；同一个文件同时使用两者时，其中一个需要写全限定名。
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
 * <p>处理器由 MyBatis 反射实例化，只能在 {@code @Results} 或 XML 中显式指定。MyBatis 会按属性类型调用 {@code (Class)} 构造器，参数为 Bean 的 {@code Class}：
 * <pre>{@code
 * @Results(id = "foo", value = {
 *         @Result(column = "payload", property = "payload", typeHandler = JsonTypeHandler.class),
 * })
 * }</pre>
 *
 * <p>PostgreSQL 文本列与 MySQL JSON 列使用同一个处理器，不需要按数据库区分：
 * <pre>{@code
 * @Insert("insert into foo(payload) values (#{payload, typeHandler = me.acomma.duck.mybatis.type.JsonTypeHandler})")
 * }</pre>
 *
 * <h2>集合列</h2>
 * <p>带类型参数的类在运行期被擦除，直接用 {@code Class} 构造 {@link JsonTypeHandler} 会被拒绝：集合/Map、{@code Iterable}、{@code Optional}、{@code Map.Entry}、自定义泛型容器/泛型 Bean 都一样。
 * 元素为这些类型的数组（如 {@code List<Foo>[]}）也会被拒绝；需要用 {@link com.fasterxml.jackson.core.type.TypeReference} 固定完整类型。
 * 具体集合/Map 子类如果自身没有类型参数（如 {@code class Tags extends ArrayList<String>}）可以用 {@code Class}：
 * <pre>{@code
 * public class FooListArrayJsonTypeHandler extends JsonTypeHandler<List<Foo>[]> {
 *     public FooListArrayJsonTypeHandler() {
 *         super(new TypeReference<List<Foo>[]>() {});
 *     }
 * }
 * }</pre>
 * 本包已为以下元素类型预置了文本绑定的集合处理器：String、Boolean、Character、Byte、Short、Integer、Long、Float、Double、BigDecimal、BigInteger、UUID、LocalDate、LocalDateTime、LocalTime、OffsetDateTime、OffsetTime、Instant、ZonedDateTime、Duration、Period、Year、YearMonth、Date（{@link java.util.Date}）、URI（{@link java.net.URI}）、Currency（{@link java.util.Currency}）。
 * 用法与 {@link JsonTypeHandler} 相同：
 * <pre>{@code
 * @Results(id = "foo", value = {
 *         @Result(column = "tags", property = "tags", typeHandler = StringListJsonTypeHandler.class),
 * })
 * @Insert("insert into foo(tags) values (#{tags, typeHandler = me.acomma.duck.mybatis.type.StringListJsonTypeHandler})")
 * }</pre>
 *
 * <p>每个预置集合处理器都有无参和 {@code (ObjectMapper)} 两个构造器：无参使用全局默认 mapper，{@code (ObjectMapper)} 供直接实例化或自定义子类使用，例如 {@code new StringListJsonTypeHandler(objectMapper)}。
 * MyBatis 的 {@code @Results}/XML 只引用 handler 类，仍走无参构造器；要在单个字段换 mapper，写一个调用 {@code super(mapper)} 的小子类即可。
 *
 * <p>自定义元素类型（如 {@code List<Foo>}）没有预置实现，继承 {@link JsonTypeHandler} 并用 {@link com.fasterxml.jackson.core.type.TypeReference} 固定完整类型：
 * <pre>{@code
 * public class FooListJsonTypeHandler extends JsonTypeHandler<List<Foo>> {
 *     public FooListJsonTypeHandler() {
 *         super(new TypeReference<List<Foo>>() {});
 *     }
 * }
 * }</pre>
 * 本包预置的这些集合处理器和自定义子类都不要通过 {@code mybatis.type-handlers-package} 注册。MyBatis 按处理器直接父类的第一个泛型实参确定注册键：
 * 按这里的写法（{@code extends JsonTypeHandler<List<Foo>>}）键是擦除后的 {@code java.util.List}，所以即使只有一种元素类型，它也会接管所有 {@code List} 属性；不同元素类型之间还会互相覆盖。
 * 本包的 {@link JsonTypeHandler} 没有无参构造器，扫描本包本身也会直接抛 {@code TypeException}。
 * 集合处理器只在 {@code @Results} / XML 中显式指定。不要依赖这个键的具体值：它由直接父类的泛型实参决定，类层次一变就可能变。
 *
 * <p>其他泛型（如 {@code Map<String, Foo>}）用同样的方式固定类型：
 * <pre>{@code
 * public class FooMapJsonTypeHandler extends JsonTypeHandler<Map<String, Foo>> {
 *     public FooMapJsonTypeHandler() {
 *         super(new TypeReference<Map<String, Foo>>() {});
 *     }
 * }
 * }</pre>
 *
 * <h2>容器建模</h2>
 * <p>JSON 只有 object 和 array 两种复合结构，Java 侧选什么容器是建模问题：
 * <ul>
 * <li><b>JSON object</b>：已知字段优先定义具体类；只有 key 不固定（metadata、extensions、headers 等）才用 {@code Map}。{@code Map<String,String>} 这类已知键值也优先具体类。</li>
 * <li><b>JSON array</b>：优先 {@code List}；它和 JSON 数组一一对应。{@code Set} 不是 JSON 原生类型，普通 {@code Set}（如 {@code HashSet}）序列化成数组后没有顺序保证，反序列化还会静默丢弃重复元素；唯一性应由领域层校验。</li>
 * </ul>
 * <p>本包不预置 {@code Map}/{@code Set} 处理器；需要动态对象或确实需要 Set 语义时，用 {@link com.fasterxml.jackson.core.type.TypeReference} 自定义。例如：
 * <pre>{@code
 * public class StringObjectMapJsonTypeHandler extends JsonTypeHandler<Map<String, Object>> {
 *     public StringObjectMapJsonTypeHandler() {
 *         super(new TypeReference<Map<String, Object>>() {});
 *     }
 * }
 *
 * public class FooSetJsonTypeHandler extends JsonTypeHandler<Set<Foo>> {
 *     public FooSetJsonTypeHandler() {
 *         super(new TypeReference<Set<Foo>>() {});
 *     }
 * }
 * }</pre>
 * <p>需要保留任意 JSON 树（嵌套数组、数字精度）时，单个字段可以直接用 {@code JsonNode} 配合 {@link JsonTypeHandler}（{@code JsonNode} 不是 Collection/Map，{@code Class} 构造器可用）；{@code List<JsonNode>} 用 {@code TypeReference} 自定义。
 * 使用 {@code Set} 时实体字段最好声明为 {@code LinkedHashSet}/{@code TreeSet}（后者要求元素可比较）并用对应的 {@code TypeReference} 固定顺序；普通 {@code Set} 的反序列化实现由 Jackson 决定，通常不保证顺序，元素还需要正确的 {@code equals}/{@code hashCode}，否则去重结果不可靠。
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
 * <li>{@code Class} 构造器拒绝任何带类型参数的类（Collection/Map、{@code Iterable}、{@code Optional}、{@code Map.Entry}、自定义泛型 Bean/容器）及其数组（{@code Class} 自身是例外，按标量目标处理）；具体集合/Map 子类如果自身没有类型参数（如 {@code class Tags extends ArrayList<String>}）可以用 {@code Class}。{@code Map}/{@code Set} 容器没有预置，请按“容器建模”用 {@link com.fasterxml.jackson.core.type.TypeReference} 自定义。</li>
 * </ul>
 */
package me.acomma.duck.mybatis.type;

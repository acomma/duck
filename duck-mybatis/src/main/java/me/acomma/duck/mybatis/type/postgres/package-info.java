/**
 * PostgreSQL {@code json} / {@code jsonb} 列的 MyBatis 类型处理器。
 *
 * <h2>前置条件</h2>
 * <ul>
 * <li>运行期需要 PostgreSQL JDBC 驱动（{@code org.postgresql:postgresql}）。本模块以 optional 依赖声明，不会传递给使用方，使用方须自行引入；未引入时，第一次走非空写入路径会抛 {@link java.lang.NoClassDefFoundError}。</li>
 * <li>仅适用于 PostgreSQL 原生的 {@code json}、{@code jsonb} 列。{@code text}/{@code varchar} 等文本列、MySQL 的 {@code JSON} 列请使用根包的 {@link me.acomma.duck.mybatis.type.JsonTypeHandler}，它按字符串绑定且不依赖 PostgreSQL JDBC 驱动。</li>
 * <li>SQL NULL 统一按 {@link java.sql.Types#OTHER} 绑定，避免 PostgreSQL 把 NULL 推断成 {@code character varying}。</li>
 * </ul>
 *
 * <p>本包与父包都使用短类名（{@code BaseJsonTypeHandler}、{@code JsonTypeHandler}，以及 {@code <元素类型>ListJsonTypeHandler}），靠包名区分：
 * 本包的类处理 PostgreSQL 原生 {@code json}/{@code jsonb}，父包的 {@link me.acomma.duck.mybatis.type.BaseJsonTypeHandler} 是默认文本绑定的基类、{@link me.acomma.duck.mybatis.type.JsonTypeHandler} 是文本处理器；
 * 父包的 {@code <元素类型>ListJsonTypeHandler} 也是文本绑定，本包的同名类只处理 PG 原生 {@code json}，{@code <元素类型>ListJsonbTypeHandler} 处理 {@code jsonb}。
 * 同一个文件同时使用两者时，其中一个需要写全限定名。
 *
 * <h2>基本用法</h2>
 * <p>处理器由 MyBatis 反射实例化，只能在 {@code @Results} 或 XML 中显式指定：
 * <pre>{@code
 * @Results(id = "foo", value = {
 *         @Result(column = "payload", property = "payload", typeHandler = JsonbTypeHandler.class),
 * })
 * }</pre>
 *
 * <h2>文本列与 MySQL</h2>
 * <p>{@code text}/{@code varchar} 列保存 JSON 时不要使用本包的处理器：它们按 {@code PGobject} 绑定 {@code json}/{@code jsonb}，与文本列的类型不匹配。
 * 改用根包的 {@link me.acomma.duck.mybatis.type.JsonTypeHandler}，它按字符串绑定，同样适用于 MySQL 5.7.8+ 的 {@code JSON} 列和 MariaDB 的 {@code JSON}（{@code LONGTEXT} 别名）。
 * 文本列需要集合处理器时，用根包的同名 {@code <元素类型>ListJsonTypeHandler}（文本绑定）；本包的 {@code <元素类型>ListJsonTypeHandler} 只处理 PG 原生 {@code json}。
 * MySQL 没有 {@code jsonb} 类型，因此不存在对应的 {@code JsonbTypeHandler} 实现。
 *
 * <h2>集合列</h2>
 * <p>带类型参数的类在运行期被擦除，{@link JsonTypeHandler}、{@link JsonbTypeHandler} 的 {@code Class} 构造器无法还原实参：集合/Map、{@code Iterable}、{@code Optional}、{@code Map.Entry}、自定义泛型容器/泛型 Bean 都会被拒绝；元素为这些类型的数组（如 {@code List<Foo>[]}）也一样。
 * 具体集合/Map 子类如果自身没有类型参数可以用 {@code Class}；否则用 {@link com.fasterxml.jackson.core.type.TypeReference} 固定完整类型，或继承处理器写自定义子类。
 * 因此每种元素类型都有两个固定了元素类型的实现：json 列为 {@code <元素类型>ListJsonTypeHandler}，jsonb 列为 {@code <元素类型>ListJsonbTypeHandler}。它们直接继承 {@link JsonTypeHandler} / {@link JsonbTypeHandler}，并在构造器里用 {@link com.fasterxml.jackson.core.type.TypeReference} 固定 {@code List<元素类型>}。
 * 已覆盖的元素类型：String、Boolean、Character、Byte、Short、Integer、Long、Float、Double、BigDecimal、BigInteger、UUID、LocalDate、LocalDateTime、LocalTime、OffsetDateTime、OffsetTime、Instant、ZonedDateTime、Duration、Period、Year、YearMonth、Date（{@link java.util.Date}）、URI（{@link java.net.URI}）、Currency（{@link java.util.Currency}）。
 * 同样的元素类型在根包也有文本绑定的 {@code <元素类型>ListJsonTypeHandler}；本包的 json 版与它同名，jsonb 版是 {@code <元素类型>ListJsonbTypeHandler}。文本列不要误用本包的 json 版。
 *
 * <p>每个预置集合处理器都有无参和 {@code (ObjectMapper)} 两个构造器：无参使用全局默认 mapper，{@code (ObjectMapper)} 供直接实例化或自定义子类使用，例如 {@code new StringListJsonbTypeHandler(objectMapper)}。
 * MyBatis 的注解/XML 仍走无参构造器；要在单个字段换 mapper，写一个调用 {@code super(mapper)} 的小子类即可。
 *
 * <p>{@code Map}/{@code Set} 没有预置：JSON array 优先 {@code List}；Map 只用于动态 object；需要时按根包 {@code type/package-info} 的“容器建模”用 {@link com.fasterxml.jackson.core.type.TypeReference} 自定义。
 * PG 侧用本包的 {@link JsonTypeHandler}/{@link JsonbTypeHandler} 固定同样的 {@code TypeReference} 即可。
 *
 * <p>集合列读写都必须显式指定处理器：
 * <pre>{@code
 * @Results(id = "foo", value = {
 *         @Result(column = "tags", property = "tags", typeHandler = StringListJsonbTypeHandler.class),
 * })
 * @Insert("insert into foo(tags) values (#{tags, typeHandler = me.acomma.duck.mybatis.type.postgres.StringListJsonbTypeHandler})")
 * }</pre>
 *
 * <p>自定义元素类型（如 {@code List<Foo>}）没有预置实现，继承 {@link JsonTypeHandler} 或 {@link JsonbTypeHandler} 即可：
 * <pre>{@code
 * public class FooListJsonbTypeHandler extends JsonbTypeHandler<List<Foo>> {
 *     public FooListJsonbTypeHandler() {
 *         super(new TypeReference<List<Foo>>() {});
 *     }
 * }
 * }</pre>
 *
 * <h2>日期时间</h2>
 * <p>日期时间按 ISO-8601 字符串读写并保留时区信息：{@code ZonedDateTime} 带时区 ID，{@code Duration} 用 ISO-8601 时长（{@code PT1M30S}），{@code Date} 按 UTC 时间点读写。
 *
 * <h2>全局 ObjectMapper</h2>
 * <p>MyBatis 自己实例化类型处理器，不经过 Spring 等容器。未显式传入 {@link com.fasterxml.jackson.databind.ObjectMapper} 的构造器在构造时取 {@link ObjectMapperHolder#getDefault()}；应用在启动早期、解析 mapper 之前调用一次 {@link ObjectMapperHolder#setDefault(com.fasterxml.jackson.databind.ObjectMapper)} 设置默认实现即可。
 * 根包的 {@link me.acomma.duck.mybatis.type.JsonTypeHandler} 共用同一套规则。
 *
 * <h3>Spring Boot</h3>
 * <p>放进 {@code ConfigurationCustomizer}：{@code MybatisAutoConfiguration} 构建 {@code SqlSessionFactory} 时会先应用全部 customizer，再解析 XML mapper（此时才构造处理器），这个时机是安全的。
 * <pre>{@code
 * @Bean
 * ConfigurationCustomizer duckJsonObjectMapperCustomizer() {
 *     return configuration -> ObjectMapperHolder.setDefault(ObjectMapperHolder.createDefault().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
 * }
 * }</pre>
 * <p>不要声明 {@code ObjectMapper} 类型的 bean：Spring Boot 的 {@code JacksonAutoConfiguration} 带 {@code @ConditionalOnMissingBean}，会被顶掉，{@code spring.jackson.*} 与 HTTP 层用到的 mapper 一起失效。
 * 需要沿用应用的 Jackson 约定时，从 {@code Jackson2ObjectMapperBuilder} 构建后调用 {@code setDefault}。
 *
 * <h3>普通 MyBatis</h3>
 * <p>在 {@code SqlSessionFactoryBuilder#build} 之前设置：
 * <pre>{@code
 * ObjectMapperHolder.setDefault(ObjectMapperHolder.createDefault().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
 *
 * // 之后才解析 mapper、构造处理器
 * SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
 * }</pre>
 *
 * <h3>替换与清理</h3>
 * <p>设置是替换而不是合并：传入的 mapper 不会被库修改，也不继承 {@link ObjectMapperHolder#createDefault()} 的基线配置，建议从 {@code createDefault()} 起手。之后再替换只影响新构造的处理器，已构造的处理器继续使用自己的 mapper。
 * 全局默认值只有一份：不同 {@code SqlSessionFactory} 需要不同配置时，用显式传入 mapper 的构造器（或继承处理器调用 {@code super(mapper)}），不要依赖全局默认值。
 * {@link ObjectMapperHolder#resetDefault()} 用于上下文关闭/测试清理。
 *
 * <h2>限制</h2>
 * <ul>
 * <li>{@link JsonTypeHandler}、{@link JsonbTypeHandler} 只有带参构造器，{@code mybatis.type-handlers-package} 的包扫描无法实例化它们，必须由 MyBatis 按 {@code (Class)} 构造器创建。</li>
 * <li>集合处理器虽然有无参构造器，也只能显式指定：如果把它们单独放进一个被扫描的包，MyBatis 会按直接父类的泛型实参把它们注册到 {@code java.util.List} 上，所有 {@code List} 属性都会拿到最后注册的处理器，不同元素类型、json/jsonb 变体之间还会互相覆盖。扫描整个本包则会先被上一条的 {@link JsonTypeHandler}/{@link JsonbTypeHandler} 挡住。</li>
 * <li>显式传入 {@link com.fasterxml.jackson.databind.ObjectMapper} 的构造器（{@code new JsonbTypeHandler<>(objectMapper, Foo.class)}、{@code new StringListJsonbTypeHandler(objectMapper)}）优先，且不会使用全局默认值。</li>
 * <li>{@code Class} 构造器拒绝任何带类型参数的类（Collection/Map、{@code Iterable}、{@code Optional}、{@code Map.Entry}、自定义泛型 Bean/容器）及其数组（{@code Class} 自身是例外，按标量目标处理）；具体集合/Map 子类如果自身没有类型参数（如 {@code class Tags extends ArrayList<String>}）可以用 {@code Class}。{@code Map}/{@code Set} 容器没有预置，请按根包的“容器建模”用 {@link com.fasterxml.jackson.core.type.TypeReference} 自定义。</li>
 * <li>全局默认值只有一份；不同 {@code SqlSessionFactory} 需要不同 JSON 格式时，用显式传入 mapper 的处理器子类，不要依赖 {@code setDefault}。一个 {@code SqlSessionFactory} + 动态数据源（如 {@code DynamicDataSource}）仍然只有一份 {@code Configuration}，同样需要显式 mapper 子类。</li>
 * </ul>
 */
package me.acomma.duck.mybatis.type.postgres;

import me.acomma.duck.mybatis.type.ObjectMapperHolder;

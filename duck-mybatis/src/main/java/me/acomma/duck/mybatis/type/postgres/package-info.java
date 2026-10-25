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
 * <p>本包与父包都使用短类名（{@code BaseJsonTypeHandler}、{@code JsonTypeHandler}、{@code JsonbTypeHandler}），靠包名区分：
 * 本包的类处理 PostgreSQL 原生 {@code json}/{@code jsonb}，父包的 {@link me.acomma.duck.mybatis.type.BaseJsonTypeHandler} 是默认文本绑定的基类、{@link me.acomma.duck.mybatis.type.JsonTypeHandler} 是文本处理器。
 * 同一个文件同时使用两者时，其中一个需要写全限定名。</p>
 *
 * <h2>基本用法</h2>
 * <p>处理器由 MyBatis 反射实例化，只能在 {@code @Results} 或 XML 中显式指定：
 * <pre>{@code
 * @Results(id = "foo", value = {
 *         @Result(column = "payload", property = "payload", typeHandler = JsonbTypeHandler.class),
 * })
 * }</pre>
 *
 * <h2>一个处理器覆盖所有目标类型</h2>
 * <p>MyBatis 3.6.0 的 {@code TypeHandlerRegistry} 在实例化处理器时优先查找 {@code (java.lang.reflect.Type)} 构造器，并把属性或参数的完整泛型类型传进来。
 * 因此 {@link JsonTypeHandler} / {@link JsonbTypeHandler} 一个类就能处理 Bean、{@code List<Foo>}、{@code Map<String, Object>}、{@code List<Foo>[]}，不需要固定元素类型的子类，也不需要 {@code TypeReference}：
 * <pre>{@code
 * public class Foo {
 *     private Payload payload;             // jsonb -> 具体类
 *     private List<Foo> children;          // jsonb -> List<Foo>
 *     private Map<String, Object> extra;   // jsonb -> Map<String, Object>
 * }
 *
 * @Results(id = "foo", value = {
 *         @Result(column = "payload", property = "payload", typeHandler = JsonbTypeHandler.class),
 *         @Result(column = "children", property = "children", typeHandler = JsonbTypeHandler.class),
 *         @Result(column = "extra", property = "extra", typeHandler = JsonbTypeHandler.class),
 * })
 * }</pre>
 * <p>目标类型用 {@link java.lang.reflect.Type} 描述：{@link Class} 用于普通 Bean、标量与无类型参数的集合/Map 子类；{@link java.lang.reflect.ParameterizedType}、{@link java.lang.reflect.GenericArrayType} 用于集合、{@code Map}、{@code Optional} 等泛型目标。
 * 用原始 {@code Class} 描述带类型参数的类仍会被拒绝（{@link BaseJsonTypeHandler} 继承的校验），正常写法不会触发。详见根包 {@code type/package-info} 的“目标类型”一节。
 *
 * <h2>文本列与 MySQL</h2>
 * <p>{@code text}/{@code varchar} 列保存 JSON 时不要使用本包的处理器：它们按 {@code PGobject} 绑定 {@code json}/{@code jsonb}，与文本列的类型不匹配。
 * 改用根包的 {@link me.acomma.duck.mybatis.type.JsonTypeHandler}，它按字符串绑定，同样适用于 MySQL 5.7.8+ 的 {@code JSON} 列和 MariaDB 的 {@code JSON}（{@code LONGTEXT} 别名）。
 * MySQL 没有 {@code jsonb} 类型，因此不存在对应的 {@code JsonbTypeHandler} 实现。
 *
 * <h2>日期时间</h2>
 * <p>日期时间按 ISO-8601 字符串读写并保留时区信息：{@code ZonedDateTime} 带时区 ID，{@code Duration} 用 ISO-8601 时长（{@code PT1M30S}），{@code Date} 按 UTC 时间点读写。根包的处理器共用同一套 Jackson 配置。
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
 * 全局默认值只有一份：不同 {@code SqlSessionFactory} 需要不同配置时，用显式传入 mapper 的构造器（或继承处理器调用 {@code super(mapper, ...)}），不要依赖全局默认值。
 * {@link ObjectMapperHolder#resetDefault()} 用于上下文关闭/测试清理。
 *
 * <h2>限制</h2>
 * <ul>
 * <li>{@link JsonTypeHandler}、{@link JsonbTypeHandler} 只有带参构造器，{@code mybatis.type-handlers-package} 的包扫描无法正确注册它们：注册键由 {@code TypeParameterResolver#resolveClassTypeParams} 推导，本包处理器的类型参数解析不出具体类型，会被当成 {@code Object} 注册成“智能处理器”，此后任何落到 {@code Object} 或原始类型的查找都会实例化本包处理器并报错。请只在 {@code @Results} / XML / {@code #{...}} 中显式指定。</li>
 * <li>自动映射默认找不到本包的处理器；确需自动映射时，在构造 {@code SqlSessionFactory} 前用 {@code TypeHandlerRegistry#register(java.lang.reflect.Type, Class)} 显式注册目标类型，示例见根包 {@code type/package-info}。</li>
 * <li>显式传入 {@link com.fasterxml.jackson.databind.ObjectMapper} 的构造器（如 {@code new JsonbTypeHandler<>(objectMapper, Foo.class)}）优先，且不会使用全局默认值。</li>
 * <li>全局默认值只有一份；不同 {@code SqlSessionFactory} 需要不同 JSON 格式时，用显式传入 mapper 的处理器子类，不要依赖 {@code setDefault}。一个 {@code SqlSessionFactory} + 动态数据源（如 {@code DynamicDataSource}）仍然只有一份 {@code Configuration}，同样需要显式 mapper 子类。</li>
 * </ul>
 */
package me.acomma.duck.mybatis.type.postgres;

import me.acomma.duck.mybatis.type.ObjectMapperHolder;

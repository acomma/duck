package me.acomma.duck.mybatis.type;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;

/**
 * 以文本形式保存 JSON 的列的类型处理器。
 *
 * <p>绑定逻辑由 {@link BaseJsonTypeHandler} 的默认文本绑定提供：写入按字符串绑定，SQL NULL 按 {@link java.sql.Types#VARCHAR} 写入，读取时按字符串读出后再反序列化，因此不依赖任何数据库厂商的 JDBC 扩展类型。
 * 本类只提供公开构造器，便于 MyBatis 按属性类型实例化。
 *
 * <p>适用于：
 * <ul>
 * <li>PostgreSQL 的 {@code text}、{@code varchar} 列（注意：{@code json}/{@code jsonb} 列请使用 {@link me.acomma.duck.mybatis.type.postgres.JsonTypeHandler} / {@link me.acomma.duck.mybatis.type.postgres.JsonbTypeHandler}）；</li>
 * <li>MySQL 5.7.8+ 的 {@code JSON} 列：MySQL 会把绑定的字符串按 JSON 解析并校验，没有 {@code jsonb} 类型；</li>
 * <li>MariaDB 的 {@code JSON}（本质是带 {@code json_valid} 约束的 {@code LONGTEXT}）以及其他以字符串保存 JSON 的列。</li>
 * </ul>
 *
 * <p>注意：{@code type.postgres} 中也有一个同名的 {@code JsonTypeHandler}，那个只处理 PostgreSQL 原生 {@code json} 列；本类处理按字符串绑定的 JSON 列，两者不能混用。简单名相同，靠包名区分。
 *
 * <h2>用法</h2>
 * <p>MyBatis 3.6.0 起会把属性/参数的完整泛型类型传给 {@link #JsonTypeHandler(Type)}，因此 Bean、集合、{@code Map}、泛型数组都用同一个处理器，不需要为每种元素类型准备子类：
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
 * <p>写入方向同理：
 * <pre>{@code
 * @Insert("insert into foo(children) values (#{children, typeHandler = me.acomma.duck.mybatis.type.JsonTypeHandler})")
 * }</pre>
 *
 * <p>另需注意：Java 类型为 {@code String} 时，字符串会被当作普通 JSON 值序列化，最终写入的是带引号的 JSON 字符串，而不是原样写入字符串内容。如果字段本身就是一段已经合法的 JSON 文本，请使用 MyBatis 的 {@code StringTypeHandler}，不要套用本处理器。
 *
 * <p>处理器由 MyBatis 反射实例化，不需要（也不要）通过 {@code mybatis.type-handlers-package} 注册；只在 {@code @Results} / XML / {@code #{...}} 中显式指定即可。
 *
 * @param <T> JSON 列对应的 Java 类型；只影响本类在 Java 侧的静态类型，运行期类型由构造器参数决定
 */
public class JsonTypeHandler<T> extends BaseJsonTypeHandler<T> {
    /**
     * @param type 目标 Java 类型；带类型参数的类请改用 {@link #JsonTypeHandler(Type)}
     */
    public JsonTypeHandler(Class<T> type) {
        super(type);
    }

    /**
     * @param objectMapper 本处理器专用的 mapper，不能为 {@code null}
     * @param type         目标 Java 类型；带类型参数的类请改用 {@link #JsonTypeHandler(ObjectMapper, Type)}
     */
    public JsonTypeHandler(ObjectMapper objectMapper, Class<T> type) {
        super(objectMapper, type);
    }

    /**
     * MyBatis 3.6.0 起按属性/参数的泛型类型反射调用本构造器，集合、{@code Map}、泛型数组等目标类型都由它处理。
     *
     * @param type 目标 Java 类型，可以是 {@link Class}、{@link java.lang.reflect.ParameterizedType}、{@link java.lang.reflect.GenericArrayType}
     */
    public JsonTypeHandler(Type type) {
        super(type);
    }

    /**
     * @param objectMapper 本处理器专用的 mapper，不能为 {@code null}
     * @param type         目标 Java 类型，可以是 {@link Class}、{@link java.lang.reflect.ParameterizedType}、{@link java.lang.reflect.GenericArrayType}
     */
    public JsonTypeHandler(ObjectMapper objectMapper, Type type) {
        super(objectMapper, type);
    }
}

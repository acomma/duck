package me.acomma.duck.mybatis.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
 * <p>用法示例（PostgreSQL 文本列或 MySQL JSON 列相同）：
 * <pre>{@code
 * @Results(id = "foo", value = {
 *         @Result(column = "payload", property = "payload", typeHandler = JsonTypeHandler.class),
 * })
 * }</pre>
 *
 * <p>另需注意：Java 类型为 {@code String} 时，字符串会被当作普通 JSON 值序列化，最终写入的是带引号的 JSON 字符串，而不是原样写入字符串内容。如果字段本身就是一段已经合法的 JSON 文本，请使用 MyBatis 的 {@code StringTypeHandler}，不要套用本处理器。
 *
 * @param <T> JSON 列对应的 Java 类型
 */
public class JsonTypeHandler<T> extends BaseJsonTypeHandler<T> {
    public JsonTypeHandler(Class<T> type) {
        super(type);
    }

    public JsonTypeHandler(ObjectMapper objectMapper, Class<T> type) {
        super(objectMapper, type);
    }

    public JsonTypeHandler(TypeReference<T> typeReference) {
        super(typeReference);
    }

    public JsonTypeHandler(ObjectMapper objectMapper, TypeReference<T> typeReference) {
        super(objectMapper, typeReference);
    }
}

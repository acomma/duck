package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * PostgreSQL {@code jsonb} 列的类型处理器。
 *
 * <p>用法示例：
 * <pre>{@code
 * @Results(id = "foo", value = {
 *         @Result(column = "payload", property = "payload", typeHandler = JsonbTypeHandler.class),
 * })
 * }</pre>
 *
 * @param <T> JSON 列对应的 Java 类型
 */
public class JsonbTypeHandler<T> extends BaseJsonTypeHandler<T> {
    public JsonbTypeHandler(Class<T> type) {
        super("jsonb", type);
    }

    public JsonbTypeHandler(ObjectMapper objectMapper, Class<T> type) {
        super(objectMapper, "jsonb", type);
    }

    public JsonbTypeHandler(TypeReference<T> typeReference) {
        super("jsonb", typeReference);
    }

    public JsonbTypeHandler(ObjectMapper objectMapper, TypeReference<T> typeReference) {
        super(objectMapper, "jsonb", typeReference);
    }
}

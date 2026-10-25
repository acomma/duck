package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * PostgreSQL {@code json} 列的类型处理器。
 *
 * <p>本类只处理 PostgreSQL 原生 {@code json} 列。根包也有一个同名的 {@link me.acomma.duck.mybatis.type.JsonTypeHandler}，那个按字符串绑定，适用于 {@code text}/{@code varchar}、MySQL {@code JSON} 等列；两者简单名相同，靠包名区分。
 *
 * <p>用法示例：
 * <pre>{@code
 * @Results(id = "foo", value = {
 *         @Result(column = "payload", property = "payload", typeHandler = JsonTypeHandler.class),
 * })
 * }</pre>
 *
 * @param <T> JSON 列对应的 Java 类型
 */
public class JsonTypeHandler<T> extends BaseJsonTypeHandler<T> {
    public JsonTypeHandler(Class<T> type) {
        super("json", type);
    }

    public JsonTypeHandler(ObjectMapper objectMapper, Class<T> type) {
        super(objectMapper, "json", type);
    }

    public JsonTypeHandler(TypeReference<T> typeReference) {
        super("json", typeReference);
    }

    public JsonTypeHandler(ObjectMapper objectMapper, TypeReference<T> typeReference) {
        super(objectMapper, "json", typeReference);
    }
}

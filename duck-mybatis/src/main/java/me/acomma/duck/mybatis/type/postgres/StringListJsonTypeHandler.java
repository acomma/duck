package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * PostgreSQL {@code json} 列与 {@code List<String>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["foo", "bar"]}；用法见 {@link JsonTypeHandler}。
 */
public class StringListJsonTypeHandler extends JsonTypeHandler<List<String>> {
    public StringListJsonTypeHandler() {
        super(new TypeReference<List<String>>() {});
    }

    public StringListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<String>>() {});
    }
}

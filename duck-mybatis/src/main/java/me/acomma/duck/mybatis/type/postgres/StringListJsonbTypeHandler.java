package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<String>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["foo", "bar"]}；用法见 {@link JsonbTypeHandler}。
 */
public class StringListJsonbTypeHandler extends JsonbTypeHandler<List<String>> {
    public StringListJsonbTypeHandler() {
        super(new TypeReference<List<String>>() {});
    }

    public StringListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<String>>() {});
    }
}

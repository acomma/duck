package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<Short>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code [1, 2]}；用法见 {@link JsonbTypeHandler}。
 */
public class ShortListJsonbTypeHandler extends JsonbTypeHandler<List<Short>> {
    public ShortListJsonbTypeHandler() {
        super(new TypeReference<List<Short>>() {});
    }

    public ShortListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Short>>() {});
    }
}

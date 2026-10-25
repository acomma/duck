package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<Integer>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code [1, 2]}；用法见 {@link JsonbTypeHandler}。
 */
public class IntegerListJsonbTypeHandler extends JsonbTypeHandler<List<Integer>> {
    public IntegerListJsonbTypeHandler() {
        super(new TypeReference<List<Integer>>() {});
    }

    public IntegerListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Integer>>() {});
    }
}

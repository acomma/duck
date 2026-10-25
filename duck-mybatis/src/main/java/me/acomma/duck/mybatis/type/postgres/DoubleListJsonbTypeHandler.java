package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<Double>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code [1.5, 2.5]}；用法见 {@link JsonbTypeHandler}。
 */
public class DoubleListJsonbTypeHandler extends JsonbTypeHandler<List<Double>> {
    public DoubleListJsonbTypeHandler() {
        super(new TypeReference<List<Double>>() {});
    }

    public DoubleListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Double>>() {});
    }
}

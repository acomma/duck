package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<Float>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code [1.5, 2.5]}；用法见 {@link JsonbTypeHandler}。
 */
public class FloatListJsonbTypeHandler extends JsonbTypeHandler<List<Float>> {
    public FloatListJsonbTypeHandler() {
        super(new TypeReference<List<Float>>() {});
    }

    public FloatListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Float>>() {});
    }
}

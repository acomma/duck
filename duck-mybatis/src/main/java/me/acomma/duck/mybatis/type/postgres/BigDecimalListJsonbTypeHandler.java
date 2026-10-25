package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<BigDecimal>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code [1.5, 2.5]}；用法见 {@link JsonbTypeHandler}。
 */
public class BigDecimalListJsonbTypeHandler extends JsonbTypeHandler<List<BigDecimal>> {
    public BigDecimalListJsonbTypeHandler() {
        super(new TypeReference<List<BigDecimal>>() {});
    }

    public BigDecimalListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<BigDecimal>>() {});
    }
}

package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigInteger;
import java.util.List;

/**
 * PostgreSQL {@code json} 列与 {@code List<BigInteger>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code [1, 2]}；用法见 {@link JsonTypeHandler}。
 */
public class BigIntegerListJsonTypeHandler extends JsonTypeHandler<List<BigInteger>> {
    public BigIntegerListJsonTypeHandler() {
        super(new TypeReference<List<BigInteger>>() {});
    }

    public BigIntegerListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<BigInteger>>() {});
    }
}

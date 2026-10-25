package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Currency;
import java.util.List;

/**
 * PostgreSQL {@code json} 列与 {@code List<Currency>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["CNY"]}；用法见 {@link JsonTypeHandler}。
 */
public class CurrencyListJsonTypeHandler extends JsonTypeHandler<List<Currency>> {
    public CurrencyListJsonTypeHandler() {
        super(new TypeReference<List<Currency>>() {});
    }

    public CurrencyListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Currency>>() {});
    }
}

package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Currency;
import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<Currency>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["CNY"]}；用法见 {@link JsonbTypeHandler}。
 */
public class CurrencyListJsonbTypeHandler extends JsonbTypeHandler<List<Currency>> {
    public CurrencyListJsonbTypeHandler() {
        super(new TypeReference<List<Currency>>() {});
    }

    public CurrencyListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Currency>>() {});
    }
}

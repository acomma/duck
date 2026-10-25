package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Period;
import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<Period>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["P1Y2M3D"]}；用法见 {@link JsonbTypeHandler}。
 */
public class PeriodListJsonbTypeHandler extends JsonbTypeHandler<List<Period>> {
    public PeriodListJsonbTypeHandler() {
        super(new TypeReference<List<Period>>() {});
    }

    public PeriodListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Period>>() {});
    }
}

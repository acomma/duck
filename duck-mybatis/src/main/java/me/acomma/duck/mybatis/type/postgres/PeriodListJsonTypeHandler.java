package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Period;
import java.util.List;

/**
 * PostgreSQL {@code json} 列与 {@code List<Period>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["P1Y2M3D"]}；用法见 {@link JsonTypeHandler}。
 */
public class PeriodListJsonTypeHandler extends JsonTypeHandler<List<Period>> {
    public PeriodListJsonTypeHandler() {
        super(new TypeReference<List<Period>>() {});
    }

    public PeriodListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Period>>() {});
    }
}

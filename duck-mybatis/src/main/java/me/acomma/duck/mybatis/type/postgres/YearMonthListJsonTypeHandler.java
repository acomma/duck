package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.YearMonth;
import java.util.List;

/**
 * PostgreSQL {@code json} 列与 {@code List<YearMonth>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["2024-01"]}；用法见 {@link JsonTypeHandler}。
 */
public class YearMonthListJsonTypeHandler extends JsonTypeHandler<List<YearMonth>> {
    public YearMonthListJsonTypeHandler() {
        super(new TypeReference<List<YearMonth>>() {});
    }

    public YearMonthListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<YearMonth>>() {});
    }
}

package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<java.util.Date>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["2024-01-01T02:15:30.000+00:00"]}，按 UTC 时间点读写；用法见 {@link JsonbTypeHandler}。
 */
public class DateListJsonbTypeHandler extends JsonbTypeHandler<List<Date>> {
    public DateListJsonbTypeHandler() {
        super(new TypeReference<List<Date>>() {});
    }

    public DateListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Date>>() {});
    }
}

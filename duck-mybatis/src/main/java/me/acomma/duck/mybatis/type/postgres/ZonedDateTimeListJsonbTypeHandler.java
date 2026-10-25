package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<ZonedDateTime>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["2024-01-01T10:15:30+08:00[Asia/Shanghai]"]}，时区 ID 会一并保留；用法见 {@link JsonbTypeHandler}。
 */
public class ZonedDateTimeListJsonbTypeHandler extends JsonbTypeHandler<List<ZonedDateTime>> {
    public ZonedDateTimeListJsonbTypeHandler() {
        super(new TypeReference<List<ZonedDateTime>>() {});
    }

    public ZonedDateTimeListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<ZonedDateTime>>() {});
    }
}

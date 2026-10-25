package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * PostgreSQL {@code json} 列与 {@code List<OffsetDateTime>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["2024-01-01T10:15:30+08:00"]}；用法见 {@link JsonTypeHandler}。
 */
public class OffsetDateTimeListJsonTypeHandler extends JsonTypeHandler<List<OffsetDateTime>> {
    public OffsetDateTimeListJsonTypeHandler() {
        super(new TypeReference<List<OffsetDateTime>>() {});
    }

    public OffsetDateTimeListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<OffsetDateTime>>() {});
    }
}

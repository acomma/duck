package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<LocalDateTime>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["2024-01-01T10:15:30", "2024-01-02T10:15:30"]}；用法见 {@link JsonbTypeHandler}。
 */
public class LocalDateTimeListJsonbTypeHandler extends JsonbTypeHandler<List<LocalDateTime>> {
    public LocalDateTimeListJsonbTypeHandler() {
        super(new TypeReference<List<LocalDateTime>>() {});
    }

    public LocalDateTimeListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<LocalDateTime>>() {});
    }
}

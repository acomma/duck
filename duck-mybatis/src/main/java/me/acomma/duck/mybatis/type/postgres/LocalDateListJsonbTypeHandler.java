package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<LocalDate>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["2024-01-01", "2024-01-02"]}；用法见 {@link JsonbTypeHandler}。
 */
public class LocalDateListJsonbTypeHandler extends JsonbTypeHandler<List<LocalDate>> {
    public LocalDateListJsonbTypeHandler() {
        super(new TypeReference<List<LocalDate>>() {});
    }

    public LocalDateListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<LocalDate>>() {});
    }
}

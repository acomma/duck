package me.acomma.duck.mybatis.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 文本型 JSON 列与 {@code List<LocalDate>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["2024-01-01", "2024-01-02"]}；用法见 {@link JsonTypeHandler}。
 */
public class LocalDateListJsonTypeHandler extends JsonTypeHandler<List<LocalDate>> {
    public LocalDateListJsonTypeHandler() {
        super(new TypeReference<List<LocalDate>>() {});
    }

    public LocalDateListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<LocalDate>>() {});
    }
}

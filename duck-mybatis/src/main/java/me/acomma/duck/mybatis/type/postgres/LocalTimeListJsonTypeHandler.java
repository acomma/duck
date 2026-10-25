package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalTime;
import java.util.List;

/**
 * PostgreSQL {@code json} 列与 {@code List<LocalTime>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["10:15:30", "11:15:30"]}；用法见 {@link JsonTypeHandler}。
 */
public class LocalTimeListJsonTypeHandler extends JsonTypeHandler<List<LocalTime>> {
    public LocalTimeListJsonTypeHandler() {
        super(new TypeReference<List<LocalTime>>() {});
    }

    public LocalTimeListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<LocalTime>>() {});
    }
}

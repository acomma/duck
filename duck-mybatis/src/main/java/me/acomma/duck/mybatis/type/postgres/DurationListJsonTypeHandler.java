package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;

/**
 * PostgreSQL {@code json} 列与 {@code List<Duration>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["PT1M30S"]}，即 ISO-8601 时长字符串；用法见 {@link JsonTypeHandler}。
 */
public class DurationListJsonTypeHandler extends JsonTypeHandler<List<Duration>> {
    public DurationListJsonTypeHandler() {
        super(new TypeReference<List<Duration>>() {});
    }

    public DurationListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Duration>>() {});
    }
}

package me.acomma.duck.mybatis.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

/**
 * 文本型 JSON 列与 {@code List<Instant>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["2024-01-01T02:15:30Z"]}；用法见 {@link JsonTypeHandler}。
 */
public class InstantListJsonTypeHandler extends JsonTypeHandler<List<Instant>> {
    public InstantListJsonTypeHandler() {
        super(new TypeReference<List<Instant>>() {});
    }

    public InstantListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Instant>>() {});
    }
}

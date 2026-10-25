package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<Instant>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["2024-01-01T02:15:30Z"]}；用法见 {@link JsonbTypeHandler}。
 */
public class InstantListJsonbTypeHandler extends JsonbTypeHandler<List<Instant>> {
    public InstantListJsonbTypeHandler() {
        super(new TypeReference<List<Instant>>() {});
    }

    public InstantListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Instant>>() {});
    }
}

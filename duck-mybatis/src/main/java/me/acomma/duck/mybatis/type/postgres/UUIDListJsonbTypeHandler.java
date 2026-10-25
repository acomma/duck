package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<UUID>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["6f1e6e4c-1f0e-4d3f-9f4a-2b7c8d9e0f12"]}；用法见 {@link JsonbTypeHandler}。
 */
public class UUIDListJsonbTypeHandler extends JsonbTypeHandler<List<UUID>> {
    public UUIDListJsonbTypeHandler() {
        super(new TypeReference<List<UUID>>() {});
    }

    public UUIDListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<UUID>>() {});
    }
}

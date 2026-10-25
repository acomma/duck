package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.OffsetTime;
import java.util.List;

/**
 * PostgreSQL {@code json} 列与 {@code List<OffsetTime>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["10:15:30+08:00"]}；用法见 {@link JsonTypeHandler}。
 */
public class OffsetTimeListJsonTypeHandler extends JsonTypeHandler<List<OffsetTime>> {
    public OffsetTimeListJsonTypeHandler() {
        super(new TypeReference<List<OffsetTime>>() {});
    }

    public OffsetTimeListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<OffsetTime>>() {});
    }
}

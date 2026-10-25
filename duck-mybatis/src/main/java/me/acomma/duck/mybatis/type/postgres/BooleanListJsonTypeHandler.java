package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * PostgreSQL {@code json} 列与 {@code List<Boolean>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code [true, false]}；用法见 {@link JsonTypeHandler}。
 */
public class BooleanListJsonTypeHandler extends JsonTypeHandler<List<Boolean>> {
    public BooleanListJsonTypeHandler() {
        super(new TypeReference<List<Boolean>>() {});
    }

    public BooleanListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Boolean>>() {});
    }
}

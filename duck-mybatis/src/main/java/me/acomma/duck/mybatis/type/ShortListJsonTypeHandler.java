package me.acomma.duck.mybatis.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * 文本型 JSON 列与 {@code List<Short>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code [1, 2]}；用法见 {@link JsonTypeHandler}。
 */
public class ShortListJsonTypeHandler extends JsonTypeHandler<List<Short>> {
    public ShortListJsonTypeHandler() {
        super(new TypeReference<List<Short>>() {});
    }

    public ShortListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Short>>() {});
    }
}

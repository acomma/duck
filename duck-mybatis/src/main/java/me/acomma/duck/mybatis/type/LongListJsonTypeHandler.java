package me.acomma.duck.mybatis.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * 文本型 JSON 列与 {@code List<Long>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code [1, 2]}；用法见 {@link JsonTypeHandler}。
 */
public class LongListJsonTypeHandler extends JsonTypeHandler<List<Long>> {
    public LongListJsonTypeHandler() {
        super(new TypeReference<List<Long>>() {});
    }

    public LongListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Long>>() {});
    }
}

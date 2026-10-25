package me.acomma.duck.mybatis.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * 文本型 JSON 列与 {@code List<Byte>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code [1, 2]}；用法见 {@link JsonTypeHandler}。
 */
public class ByteListJsonTypeHandler extends JsonTypeHandler<List<Byte>> {
    public ByteListJsonTypeHandler() {
        super(new TypeReference<List<Byte>>() {});
    }

    public ByteListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Byte>>() {});
    }
}

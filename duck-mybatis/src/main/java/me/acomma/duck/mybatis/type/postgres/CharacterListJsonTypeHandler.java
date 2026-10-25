package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * PostgreSQL {@code json} 列与 {@code List<Character>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["a", "b"]}，每个元素必须是单个字符的字符串；用法见 {@link JsonTypeHandler}。
 */
public class CharacterListJsonTypeHandler extends JsonTypeHandler<List<Character>> {
    public CharacterListJsonTypeHandler() {
        super(new TypeReference<List<Character>>() {});
    }

    public CharacterListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Character>>() {});
    }
}

package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.List;

/**
 * PostgreSQL {@code jsonb} 列与 {@code List<URI>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["https://example.com/a"]}；用法见 {@link JsonbTypeHandler}。
 */
public class URIListJsonbTypeHandler extends JsonbTypeHandler<List<URI>> {
    public URIListJsonbTypeHandler() {
        super(new TypeReference<List<URI>>() {});
    }

    public URIListJsonbTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<URI>>() {});
    }
}

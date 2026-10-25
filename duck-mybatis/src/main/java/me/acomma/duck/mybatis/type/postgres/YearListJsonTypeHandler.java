package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Year;
import java.util.List;

/**
 * PostgreSQL {@code json} 列与 {@code List<Year>} 之间的类型处理器。
 *
 * <p>JSON 值形如 {@code ["2024"]}；用法见 {@link JsonTypeHandler}。
 */
public class YearListJsonTypeHandler extends JsonTypeHandler<List<Year>> {
    public YearListJsonTypeHandler() {
        super(new TypeReference<List<Year>>() {});
    }

    public YearListJsonTypeHandler(ObjectMapper objectMapper) {
        super(objectMapper, new TypeReference<List<Year>>() {});
    }
}

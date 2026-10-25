package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;

/**
 * PostgreSQL {@code jsonb} 列的类型处理器。
 *
 * <p>用法示例：
 * <pre>{@code
 * @Results(id = "foo", value = {
 *         @Result(column = "payload", property = "payload", typeHandler = JsonbTypeHandler.class),
 * })
 * }</pre>
 *
 * <p>属性是集合、{@code Map} 或泛型数组时不需要换处理器：MyBatis 3.6.0 起会把属性的完整泛型类型传给 {@link #JsonbTypeHandler(Type)}。目标类型的说明见 {@link BaseJsonTypeHandler}。
 *
 * @param <T> JSON 列对应的 Java 类型；只影响本类在 Java 侧的静态类型，运行期类型由构造器参数决定
 */
public class JsonbTypeHandler<T> extends BaseJsonTypeHandler<T> {
    public JsonbTypeHandler(Class<T> type) {
        super("jsonb", type);
    }

    public JsonbTypeHandler(ObjectMapper objectMapper, Class<T> type) {
        super(objectMapper, "jsonb", type);
    }

    /**
     * MyBatis 3.6.0 起按属性/参数的泛型类型反射调用本构造器，集合、{@code Map}、泛型数组等目标类型都由它处理。
     *
     * @param type 目标 Java 类型，可以是 {@link Class}、{@link java.lang.reflect.ParameterizedType}、{@link java.lang.reflect.GenericArrayType}
     */
    public JsonbTypeHandler(Type type) {
        super("jsonb", type);
    }

    public JsonbTypeHandler(ObjectMapper objectMapper, Type type) {
        super(objectMapper, "jsonb", type);
    }
}

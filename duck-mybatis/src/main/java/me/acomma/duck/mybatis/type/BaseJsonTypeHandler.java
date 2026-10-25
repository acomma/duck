package me.acomma.duck.mybatis.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

/**
 * 数据库无关的 JSON 类型处理器基类，负责 Java 对象与 JSON 文本之间的序列化/反序列化、读取，以及默认的字符串绑定。
 *
 * <p>默认写入方式是按字符串绑定：非空值调用 {@link PreparedStatement#setString(int, String)}，SQL NULL 按 {@link Types#VARCHAR} 绑定。
 * 适用于 PostgreSQL 的 {@code text}/{@code varchar}、MySQL 的 {@code JSON} 等文本型列。PostgreSQL 原生处理器之所以对 NULL 使用 {@link Types#OTHER}，是为了让 PostgreSQL 在 {@code json}/{@code jsonb} 列上推断参数类型；文本型列不需要这个技巧。
 *
 * <p>需要厂商特定绑定时覆盖 {@link #setParameter} / {@link #setNonNullParameter}：PostgreSQL 原生 {@code json}/{@code jsonb} 在 {@link me.acomma.duck.mybatis.type.postgres.BaseJsonTypeHandler} 中改用 {@code PGobject} 绑定。
 *
 * <p>{@code type.postgres} 中也有一个同名的 {@link me.acomma.duck.mybatis.type.postgres.BaseJsonTypeHandler}，它继承本类并覆盖默认绑定；两者简单名相同，靠包名区分。
 *
 * <p>目标类型支持两种描述方式：
 * <ul>
 * <li>{@link Class}：适用于普通 Bean；任何带类型参数的类（集合、{@code Map}、{@code Iterable}、{@code Optional}、{@code Map.Entry}、自定义泛型容器/泛型 Bean 等）都无法在运行期保留实参，请改用 {@link TypeReference}；元素为这些类型的数组（如 {@code List<Foo>[]}）同样会被拒绝。具体集合/Map 子类如果自身没有类型参数（如 {@code class Tags extends ArrayList<String>}）仍可用 {@code Class}；{@link Class} 自身是一个例外，按标量目标处理；</li>
 * <li>{@link TypeReference}：保留完整泛型信息，例如 {@code new TypeReference<List<Foo>>() {}}。</li>
 * </ul>
 *
 * <p>未显式传入 {@link ObjectMapper} 的构造器在构造时取 {@link ObjectMapperHolder#getDefault()}。
 * 应用应在 MyBatis 解析 mapper、构造处理器之前调用一次 {@link ObjectMapperHolder#setDefault(ObjectMapper)} 配置默认 mapper；之后再替换只影响新构造的处理器。显式传入 mapper 的构造器不受全局配置影响。
 *
 * <p>本类只提供给需要自定义数据库绑定的实现继承；直接使用请选择本包的 {@link JsonTypeHandler}（按字符串绑定）或 {@code type.postgres} 包的 {@link me.acomma.duck.mybatis.type.postgres.JsonTypeHandler} / {@link me.acomma.duck.mybatis.type.postgres.JsonbTypeHandler}（PostgreSQL 原生 {@code json}/{@code jsonb}）。
 *
 * @param <T> JSON 列对应的 Java 类型
 */
public abstract class BaseJsonTypeHandler<T> extends BaseTypeHandler<T> {
    private final ObjectMapper objectMapper;
    private final JavaType javaType;

    protected BaseJsonTypeHandler(Class<T> type) {
        this(ObjectMapperHolder.getDefault(), type);
    }

    protected BaseJsonTypeHandler(ObjectMapper objectMapper, Class<T> type) {
        this(objectMapper, javaType(objectMapper, requireConcreteClass(type)));
    }

    protected BaseJsonTypeHandler(TypeReference<T> typeReference) {
        this(ObjectMapperHolder.getDefault(), typeReference);
    }

    protected BaseJsonTypeHandler(ObjectMapper objectMapper, TypeReference<T> typeReference) {
        this(objectMapper, javaType(objectMapper, typeReference));
    }

    private BaseJsonTypeHandler(ObjectMapper objectMapper, JavaType javaType) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper");
        this.javaType = Objects.requireNonNull(javaType, "javaType");
    }

    private static JavaType javaType(ObjectMapper objectMapper, Class<?> type) {
        return Objects.requireNonNull(objectMapper, "objectMapper")
                .getTypeFactory()
                .constructType(Objects.requireNonNull(type, "type"));
    }

    private static JavaType javaType(ObjectMapper objectMapper, TypeReference<?> typeReference) {
        return Objects.requireNonNull(objectMapper, "objectMapper")
                .getTypeFactory()
                .constructType(Objects.requireNonNull(typeReference, "typeReference"));
    }

    /**
     * 带类型参数的类在运行期被擦除，只传 {@code Class} 会得到 {@code List<Object>} 一类的类型，因此统一拒绝；数组先展开到元素类型再判断。
     * 具体集合/Map 子类如果自身没有类型参数（如 {@code class Tags extends ArrayList<String>}）可以用 {@code Class}；{@link Class} 自身带类型参数，但是合法的标量目标，单独放行。
     */
    protected static <E> Class<E> requireConcreteClass(Class<E> type) {
        Objects.requireNonNull(type, "type");

        Class<?> componentType = type;
        while (componentType.isArray()) {
            componentType = componentType.getComponentType();
        }

        if (componentType != Class.class && componentType.getTypeParameters().length > 0) {
            throw new IllegalArgumentException("Generic type requires a TypeReference: " + type.getTypeName());
        }
        return type;
    }

    /**
     * 数组按 Java 源码形式展示（{@code com.example.Foo[]}、{@code List<Foo>[]}），避免 Jackson {@code toCanonical()} 对数组返回 JVM 描述符（{@code [Lcom.example.Foo;}）。
     */
    private static String javaTypeName(JavaType type) {
        return type.isArrayType() ? javaTypeName(type.getContentType()) + "[]" : type.toCanonical();
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            try {
                ps.setNull(i, Types.VARCHAR);
            } catch (SQLException e) {
                throw new TypeException("Error setting null for parameter #" + i + " of a text-bound JSON column. Cause: " + e, e);
            }
            return;
        }
        super.setParameter(ps, i, parameter, jdbcType);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, serialize(parameter, i));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // columnIndex 不会被使用：int 不能传 null，而 JDBC 列索引从 1 开始，以这里用 0 作为“无索引”的占位值；deserialize 仅根据 columnName 选择描述方式。
        return deserialize(rs.getString(columnName), columnName, 0);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return deserialize(rs.getString(columnIndex), null, columnIndex);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return deserialize(cs.getString(columnIndex), null, columnIndex);
    }

    protected final String serialize(T parameter, int i) throws SQLException {
        try {
            return objectMapper.writeValueAsString(parameter);
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to serialize parameter at index " + i + " of type " + javaTypeName(javaType) + " to JSON", e);
        }
    }

    /**
     * @param columnName  列名；按索引读取时为 {@code null}
     * @param columnIndex 列索引；按列名读取时不使用（传 0）
     */
    protected final T deserialize(String json, String columnName, int columnIndex) throws SQLException {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, javaType);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            String location = columnName != null ? "column '" + columnName + "'" : "column index " + columnIndex;
            throw new SQLException("Failed to deserialize JSON value from " + location + " as " + javaTypeName(javaType), e);
        }
    }
}

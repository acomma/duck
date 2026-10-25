package me.acomma.duck.mybatis.type.postgres;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.acomma.duck.mybatis.type.ObjectMapperHolder;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.postgresql.util.PGobject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Set;

/**
 * PostgreSQL {@code json} / {@code jsonb} 列的类型处理器基类。
 *
 * <p>本类继承根包的同名 {@link me.acomma.duck.mybatis.type.BaseJsonTypeHandler}：根包负责 Jackson 序列化和默认的字符串绑定，本类覆盖 {@code setParameter} / {@code setNonNullParameter} 为 PostgreSQL 的 {@code PGobject} / {@link Types#OTHER} 绑定。
 * 两者简单名相同，靠包名区分。
 *
 * <p>写入时按 {@link PGobject} 绑定列类型，读取时按字符串读出后反序列化；具体列类型（{@code json} 或 {@code jsonb}）与目标 Java 类型由子类构造器传入。
 * 以 {@code text}/{@code varchar} 等文本列保存 JSON 时请改用根包的 {@link me.acomma.duck.mybatis.type.JsonTypeHandler}：它按字符串绑定，不依赖 PostgreSQL JDBC 驱动。
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
 * <p>已发布为公共 API 的构造器不要再修改签名，新增能力请以重载方式提供。
 *
 * @param <T> JSON 列对应的 Java 类型
 */
public abstract class BaseJsonTypeHandler<T> extends me.acomma.duck.mybatis.type.BaseJsonTypeHandler<T> {
    private static final Set<String> SUPPORTED_POSTGRES_TYPES = Set.of("json", "jsonb");

    private final String postgresType;

    protected BaseJsonTypeHandler(String postgresType, Class<T> type) {
        this(ObjectMapperHolder.getDefault(), postgresType, type);
    }

    protected BaseJsonTypeHandler(ObjectMapper objectMapper, String postgresType, Class<T> type) {
        super(objectMapper, type);
        this.postgresType = requirePostgresType(postgresType);
    }

    protected BaseJsonTypeHandler(String postgresType, TypeReference<T> typeReference) {
        this(ObjectMapperHolder.getDefault(), postgresType, typeReference);
    }

    protected BaseJsonTypeHandler(ObjectMapper objectMapper, String postgresType, TypeReference<T> typeReference) {
        super(objectMapper, typeReference);
        this.postgresType = requirePostgresType(postgresType);
    }

    private static String requirePostgresType(String postgresType) {
        Objects.requireNonNull(postgresType, "postgresType");
        if (!SUPPORTED_POSTGRES_TYPES.contains(postgresType)) {
            throw new IllegalArgumentException("postgresType must be 'json' or 'jsonb', but was '" + postgresType + "'");
        }
        return postgresType;
    }

    /**
     * SQL NULL 不能沿用调用方的 {@code jdbcType}：PostgreSQL 的 json/jsonb 参数如果按 VARCHAR 绑定 NULL，会报 “column ... is of type jsonb but expression is of type character varying”。
     * 这里统一按 {@link Types#OTHER} 绑定，让 PostgreSQL 在目标列上推断具体类型。
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            try {
                ps.setNull(i, Types.OTHER);
            } catch (SQLException e) {
                throw new TypeException("Error setting null for parameter #" + i + " with PostgreSQL type " + postgresType + ". Cause: " + e, e);
            }
            return;
        }
        super.setParameter(ps, i, parameter, jdbcType);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        PGobject value = new PGobject();
        value.setType(postgresType);
        value.setValue(serialize(parameter, i));
        ps.setObject(i, value);
    }
}

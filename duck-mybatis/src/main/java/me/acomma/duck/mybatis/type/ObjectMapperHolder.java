package me.acomma.duck.mybatis.type;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Objects;

/**
 * JSON 类型处理器共用的默认 {@link ObjectMapper}。
 *
 * <p>MyBatis 自己实例化类型处理器，不经过 Spring 等容器，因此未显式传入 mapper 的处理器在构造时从 {@link #getDefault()} 取默认值。
 * 应用只需在启动早期、MyBatis 解析 mapper 和构造处理器之前调用一次 {@link #setDefault(ObjectMapper)} 替换默认实现：
 * <pre>{@code
 * ObjectMapperHolder.setDefault(ObjectMapperHolder.createDefault().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
 *
 * // 之后才解析 mapper、构造处理器
 * SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
 * }</pre>
 *
 * <p>Spring Boot 放在 {@code ConfigurationCustomizer} 里执行；普通 MyBatis 放在 {@code SqlSessionFactoryBuilder#build} 之前。之后再替换只会影响新构造的处理器，已经构造的处理器继续使用自己的 mapper。
 *
 * <p>全局默认值只有一份。需要给单个字段或不同 {@code SqlSessionFactory} 使用不同 mapper 时，用显式传入 mapper 的处理器构造器（或继承处理器并调用 {@code super(mapper)}），不要依赖全局默认值。
 *
 * <p>{@link #setDefault(ObjectMapper)} 是替换而不是合并：传入的 mapper 不会被库修改，也不会继承 {@link #createDefault()} 的基线配置。设置前应完成配置，设置后视为不可变；Jackson 的读写本身是线程安全的，但并发修改配置不是。
 *
 * <p>holder 的读写共用一把私有锁：并发调用不会创建两个基线，也不会读到未发布的 mapper。锁只保证单次调用的原子性，不覆盖“设置默认值 + 构造处理器”的整个过程；并发替换默认值时，不同处理器可能使用不同 mapper。
 * 需要统一配置时，应在构造处理器之前完成设置。
 */
public final class ObjectMapperHolder {
    private static final Object LOCK = new Object();

    private static ObjectMapper defaultObjectMapper;

    private ObjectMapperHolder() {
    }

    /**
     * 返回一份带 duck 基线配置的新 {@link ObjectMapper}：
     * <ul>
     * <li>自动注册 classpath 上的 Jackson 模块（如 jsr310），日期时间按 ISO-8601 字符串读写；</li>
     * <li>关闭 {@code ADJUST_DATES_TO_CONTEXT_TIME_ZONE}，保留 {@code OffsetDateTime}、{@code ZonedDateTime} 等类型自带的时区信息：否则带 {@code +08:00} 的值会被归一化成 UTC，写入再读出后不再 {@code equals} 原值；</li>
     * <li>让 {@code ZonedDateTime} 写出时区 ID（{@code 2024-01-01T10:15:30+08:00[Asia/Shanghai]}），{@code Duration} 写成 ISO-8601 时长字符串（{@code PT1M30S}）而不是秒数；</li>
     * <li>反序列化忽略未知属性，便于 JSON 结构向前兼容。</li>
     * </ul>
     *
     * <p>每次调用返回新实例，使用方可以继续注册模块、修改命名策略等，不会影响内置默认值。
     */
    public static ObjectMapper createDefault() {
        return new ObjectMapper()
                .findAndRegisterModules()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
    }

    /**
     * 设置全局默认 mapper，替换内置基线或之前设置的值。
     *
     * <p>应在 MyBatis 解析 mapper、构造处理器之前调用。已构造的处理器继续使用构造时捕获的 mapper。
     *
     * @param objectMapper 新的默认 mapper，不能为 {@code null}
     * @throws NullPointerException 如果 {@code objectMapper} 为 {@code null}
     */
    public static void setDefault(ObjectMapper objectMapper) {
        Objects.requireNonNull(objectMapper, "objectMapper");
        synchronized (LOCK) {
            defaultObjectMapper = objectMapper;
        }
    }

    /**
     * 返回全局默认 mapper；未设置时创建并缓存 {@link #createDefault()} 的基线配置。
     */
    public static ObjectMapper getDefault() {
        synchronized (LOCK) {
            if (defaultObjectMapper == null) {
                defaultObjectMapper = createDefault();
            }
            return defaultObjectMapper;
        }
    }

    /**
     * 清空全局默认值，下一次 {@link #getDefault()} 重新创建基线配置。用于上下文关闭或测试清理；已有处理器继续使用构造时捕获的 mapper。
     */
    public static void resetDefault() {
        synchronized (LOCK) {
            defaultObjectMapper = null;
        }
    }
}

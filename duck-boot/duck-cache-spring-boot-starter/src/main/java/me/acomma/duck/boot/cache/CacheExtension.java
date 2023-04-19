package me.acomma.duck.boot.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存扩展. 在 Spring Cache 的基础上提供扩展功能.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface CacheExtension {
    /**
     * 缓存的生存时间, Time To Live 的缩写形式, 时间单位为秒, 默认长期有效.
     * @return 缓存的生存时间
     */
    long ttl() default 0;
}

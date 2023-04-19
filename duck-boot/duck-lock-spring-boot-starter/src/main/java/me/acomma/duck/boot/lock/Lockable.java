package me.acomma.duck.boot.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述方法的锁属性.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lockable {
    /**
     * 要锁定的对象, 支持 <a href="https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions">SpEL</a>.
     *
     * @return 锁定的对象
     */
    String key();

    /**
     * 锁的超时时间（秒）, 默认 30 秒.
     *
     * @return 超时时间（秒）
     */
    int timeout() default 30;
}

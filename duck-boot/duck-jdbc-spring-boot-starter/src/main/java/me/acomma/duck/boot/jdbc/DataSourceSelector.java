package me.acomma.duck.boot.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据源选择器, 定义方法执行时使用的数据源类型.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSourceSelector {
    /**
     * 数据源类型, 默认使用主库数据源.
     */
    DataSourceType value() default DataSourceType.PRIMARY;
}

package me.acomma.duck.util.validation;

import me.acomma.duck.util.Constant;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被注解的元素必须在适当的常量取值范围内. 应用于数值表示.
 *
 * <p>有效的常量值由 {@link #constant()}, {@link #value()}, {@link #inclusion()} 合并后排除 {@link #exclusion()} 得到.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ConstantRangeValidator.class})
public @interface ConstantRange {
    /**
     * @return 错误消息
     */
    String message() default "不在取值范围内";

    /**
     * @return 约束所属的组
     */
    Class<?>[] groups() default {};

    /**
     * @return 与约束有关的有效负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * @return 与约束关联的常量类
     */
    Class<? extends Constant> constant() default Constant.class;

    /**
     * @return 允许的常量值
     */
    int[] value() default {};

    /**
     * @return 包含的常量值
     */
    int[] inclusion() default {};

    /**
     * @return 排除的常量值
     */
    int[] exclusion() default {};
}
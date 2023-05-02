package me.acomma.duck.util.validation;

import me.acomma.duck.util.Constant;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 被注解的元素必须在适当的常量取值范围内. 应用于数值表示.
 *
 * <p>有效的常量值由 {@link #clazz()}, {@link #value()}, {@link #inclusion()} 合并后排除 {@link #exclusion()} 得到.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ConstantRange.ConstantRangeValidator.class})
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
    Class<? extends Constant<Integer>> clazz();

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

    /**
     * 常量取值范围验证器.
     */
    class ConstantRangeValidator implements ConstraintValidator<ConstantRange, Integer> {
        private List<Integer> values;

        @Override
        public void initialize(ConstantRange constraintAnnotation) {
            Class<? extends Constant<Integer>> clazz = constraintAnnotation.clazz();

            List<Integer> values = new ArrayList<>();

            if (!clazz.isInterface()) {
                Constant<Integer> constant;
                try {
                    constant = clazz.getDeclaredConstructor().newInstance();
                } catch (InvocationTargetException | NoSuchMethodException
                         | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("常量类 " + clazz.getName() + " 未实现无参构造器", e);
                }

                List<Integer> list = constant.list();
                if (list != null && list.size() > 0) {
                    values.addAll(list);
                }
            }

            int[] value = constraintAnnotation.value();
            for (int v : value) {
                if (!values.contains(v)) {
                    values.add(v);
                }
            }

            int[] inclusion = constraintAnnotation.inclusion();
            for (int v : inclusion) {
                if (!values.contains(v)) {
                    values.add(v);
                }
            }

            int[] exclusion = constraintAnnotation.exclusion();
            for (int v : exclusion) {
                values.removeIf(e -> Objects.equals(e, v));
            }

            this.values = values;
        }

        @Override
        public boolean isValid(Integer value, ConstraintValidatorContext context) {
            // 默认不验证
            if (Objects.isNull(value)) {
                return true;
            }

            return values.contains(value);
        }
    }
}
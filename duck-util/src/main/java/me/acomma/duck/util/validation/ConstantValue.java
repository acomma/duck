package me.acomma.duck.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import me.acomma.duck.util.Constant;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 被注解的元素的值必须在常量类的取值范围内。
 *
 * <p>使用示例：
 * <pre>{@code
 * public class User {
 *     @ConstantValue(clazz = Gender.class, message = "性别不在取值范围内")
 *     private String gender;
 * }
 * }</pre>
 *
 * @see Constant
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ConstantValue.Validator.class})
public @interface ConstantValue {
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
    Class<? extends Constant<?>> clazz();

    class Validator implements ConstraintValidator<ConstantValue, Object> {
        private final List<Object> values = new ArrayList<>();

        @Override
        public void initialize(ConstantValue constraintAnnotation) {
            Class<? extends Constant<?>> clazz = constraintAnnotation.clazz();
            if (!clazz.isInterface()) {
                try {
                    Constant<?> constant = clazz.getDeclaredConstructor().newInstance();
                    List<?> list = constant.list();
                    if (list != null) {
                        values.addAll(list);
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException("常量类 " + clazz.getName() + " 未实现无参构造器", e);
                }
            }
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            // 默认不验证
            if (value == null) {
                return true;
            }
            return values.contains(value);
        }
    }
}

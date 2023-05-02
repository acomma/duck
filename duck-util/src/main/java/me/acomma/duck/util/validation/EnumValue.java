package me.acomma.duck.util.validation;

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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 被注解的元素的值必须在枚举类的取值范围内。通常使用在字段类型不是枚举，比如 {@code String} 或者 {@code Integer}，同时又要要限制该字段的取值范围时。举个例子
 *
 * <pre>{@code
 * public enum Gender {
 *     MALE(1),
 *     FEMALE(2);
 *
 *     private final int code;
 *
 *     Gender(int code) {
 *         this.code = code;
 *     }
 *
 *     public int code() {
 *         return code;
 *     }
 * }
 *
 * public class User {
 *     @EnumValue(clazz = Gender.class, method = "code")
 *     private Integer gender;
 * }
 * }</pre>
 *
 * 参考资料：
 * <ul>
 *     <li><a href="https://www.jianshu.com/p/14fc8c739ce8">自定义 javax.validation 校验枚举类</a></li>
 *     <li><a href="https://www.cnblogs.com/huan1993/p/15416104.html">基于自定义 Validator 来验证枚举类型</a></li>
 *     <li><a href="https://blog.csdn.net/xiaojin21cen/article/details/102622771">Hibernate Validation 自定义枚举校验和固定值校验证</a></li>
 *     <li><a href="https://blog.csdn.net/Lieforlove/article/details/101370087">Java 中枚举类型的校验</a></li>
 * </ul>
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValue.Validator.class})
public @interface EnumValue {
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
     * @return 与约束关联的枚举类
     */
    Class<? extends Enum<?>> clazz();

    /**
     * @return 用来获取与约束关联的枚举类的值的方法
     */
    String method() default "name";

    class Validator implements ConstraintValidator<EnumValue, Object> {
        private List<Object> values;

        @Override
        public void initialize(EnumValue constraintAnnotation) {
            Class<? extends Enum<?>> clazz = constraintAnnotation.clazz();
            Object[] enums = clazz.getEnumConstants();
            Method method;
            try {
                method = clazz.getMethod(constraintAnnotation.method());
            } catch (NoSuchMethodException e1) {
                throw new RuntimeException("枚举类 " + constraintAnnotation.clazz().getName() + " 不存在 " + constraintAnnotation.method() + " 方法", e1);
            }

            List<Object> values = new ArrayList<>();
            if (enums != null) {
                for (Object e : enums) {
                    try {
                        Object v = method.invoke(e);
                        values.add(v);
                    } catch (IllegalAccessException | InvocationTargetException e2) {
                        throw new RuntimeException("调用枚举类 " + constraintAnnotation.clazz().getName() + " 的 " + constraintAnnotation.method() + " 方法失败", e2);
                    }
                }
            }

            this.values = values;
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

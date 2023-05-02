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
import java.util.ArrayList;
import java.util.List;

/**
 * 被注解的元素的值必须在指定的的取值范围内，支持 {@code Integer}、{@code Long} 和 {@code String}。举个例子
 *
 * <pre>{@code
 * public class User {
 *     @FixedValue(ints = {1, 2})
 *     private Integer gender;
 * }
 *
 * public class User {
 *     @FixedValue(longs = {1, 2})
 *     private Long gender;
 * }
 *
 * public class User {
 *     @FixedValue(strings = {"MALE", "FEMALE"})
 *     private String gender;
 * }
 * }</pre>
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {FixedValue.Validator.class})
public @interface FixedValue {
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
     * @return 允许的整型值
     */
    int[] ints() default {};

    /**
     * @return 允许的长整形值
     */
    long[] longs() default {};

    /**
     * @return 允许的字符串值
     */
    String[] strings() default {};

    class Validator implements ConstraintValidator<FixedValue, Object> {
        private List<Integer> ints;
        private List<Long> longs;
        private List<String> strings;

        @Override
        public void initialize(FixedValue constraintAnnotation) {
            int[] ints = constraintAnnotation.ints();
            long[] longs = constraintAnnotation.longs();
            String[] strings = constraintAnnotation.strings();

            List<Integer> intList = new ArrayList<>();
            for (int i : ints) {
                intList.add(i);
            }

            List<Long> longList = new ArrayList<>();
            for (long l : longs) {
                longList.add(l);
            }

            List<String> stringList = new ArrayList<>();
            for (String s : strings) {
                stringList.add(s);
            }

            this.ints = intList;
            this.longs = longList;
            this.strings = stringList;
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            // 默认不验证
            if (value == null) {
                return true;
            }

            if (value instanceof Integer) {
                return ints.contains(value);
            }

            if (value instanceof Long) {
                return longs.contains(value);
            }

            if (value instanceof String) {
                return strings.contains(value);
            }

            // 类型不匹配时校验不通过
            return false;
        }
    }
}

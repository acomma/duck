package me.acomma.duck.util.validation;

import me.acomma.duck.util.Constant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 常量取值范围验证器.
 */
public class ConstantRangeValidator implements ConstraintValidator<ConstantRange, Integer> {
    private List<Integer> values;

    @Override
    public void initialize(ConstantRange constraintAnnotation) {
        Class<? extends Constant> clazz = constraintAnnotation.constant();

        List<Integer> values = new ArrayList<>();

        if (!clazz.isInterface()) {
            Constant constant;
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

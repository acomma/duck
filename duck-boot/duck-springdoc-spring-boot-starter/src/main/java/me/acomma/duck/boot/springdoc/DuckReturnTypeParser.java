package me.acomma.duck.boot.springdoc;

import me.acomma.duck.util.RestResult;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springdoc.core.parsers.ReturnTypeParser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * 自定义返回类型解析器，实现对返回值类型不是 {@link RestResult} 和返回值类型是 {@code void} 的解析。
 */
public class DuckReturnTypeParser implements ReturnTypeParser {
    @Override
    public Type getReturnType(MethodParameter methodParameter) {
        Type returnType = ReturnTypeParser.super.getReturnType(methodParameter);
        Annotation[] annotations = Objects.requireNonNull(methodParameter.getMethod()).getDeclaringClass().getAnnotations();

        if (Arrays.stream(annotations).noneMatch(annotation -> annotation instanceof RestController)) {
            return returnType;
        }

        // 如果返回值类型形如 Result<User>，那么 returnType 的类型为 ResolvableType.SyntheticParameterizedType，
        // 但是 ResolvableType.SyntheticParameterizedType 是私有静态类，外部无法访问，因此这里不能用 == 比较
        if (returnType.getTypeName().contains("me.acomma.duck.util.RestResult")) {
            return returnType;
        }

        // returnType 在这里不会是类似 Result<Void> 的类型，也可以使用 returnType.getTypeName().equals("void") || returnType.getTypeName().equals("java.lang.Void")  进行判断，
        // 并且需要重新使用 Void.class 进行参数化，不然在后面会出现 NullPointerException
        if (returnType == void.class || returnType == Void.class) {
            return TypeUtils.parameterize(RestResult.class, Void.class);
        }

        // 泛型类类型参数化
        return TypeUtils.parameterize(RestResult.class, returnType);
    }
}

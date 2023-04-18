package me.acomma.duck.boot.web.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.acomma.duck.util.RestResult;
import me.acomma.duck.util.code.SystemErrorCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一处理响应数据的格式，代码大部分来自 <a href="https://mp.weixin.qq.com/s/zUrx7duy0-OY1oYn8FeKOw">SpringBoot 实战：一招实现结果的优雅响应</a>。
 */
@RestControllerAdvice
@Slf4j
public class RestResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 只处理 Controller 方法的返回值的类型不是 RestResult 的方法
        return !returnType.getGenericParameterType().equals(RestResult.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null || body instanceof RestResult<?>) {
            return body;
        }

        RestResult<Object> result = new RestResult<>();
        result.setCode(SystemErrorCode.SUCCESS.code());
        result.setData(body);

        // Controller 方法的返回值的类型为 String 需要特殊处理，不然会出现 ClassCastException: class RestResult cannot be cast to class String
        if (returnType.getGenericParameterType().equals(String.class)) {
            try {
                // 为了与非 String 类型时响应 Content-Type 的值一直而添加这行代码，否者响应的 Content-Type 的值为 text/plain
                response.getHeaders().setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));

                return objectMapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                // 一般不会走到这个异常分支
                log.error("将 RestResult 对象序列化为 JSON 字符串时发生异常", e);

                RestResult<Void> failedResult = new RestResult<>();
                failedResult.setCode(SystemErrorCode.PROCESS_JSON_FAILED.code());
                failedResult.setMessage(SystemErrorCode.PROCESS_JSON_FAILED.message());

                return failedResult;
            }
        }

        return result;
    }
}

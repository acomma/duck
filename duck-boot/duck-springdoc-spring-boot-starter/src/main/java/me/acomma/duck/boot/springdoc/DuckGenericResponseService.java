package me.acomma.duck.boot.springdoc;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.ArrayUtils;
import org.springdoc.core.parsers.ReturnTypeParser;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OperationService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springdoc.core.utils.SpringDocAnnotationsUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.springdoc.core.utils.SpringDocAnnotationsUtils.extractSchema;

/**
 * 返回值类型为 {@code void} 时也需要构建 {@link Content}。
 */
public class DuckGenericResponseService extends GenericResponseService {
    /**
     * Instantiates a new Generic response builder.
     *
     * @param operationService          the operation builder
     * @param returnTypeParsers         the return type parsers
     * @param springDocConfigProperties the spring doc config properties
     * @param propertyResolverUtils     the property resolver utils
     */
    public DuckGenericResponseService(OperationService operationService, List<ReturnTypeParser> returnTypeParsers, SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils) {
        super(operationService, returnTypeParsers, springDocConfigProperties, propertyResolverUtils);
    }

    @Override
    public Content buildContent(Components components, Annotation[] annotations, String[] methodProduces, JsonView jsonView, Type returnType) {
        Content content = new Content();

        // 如果 returnType 是 void，父类在这里直接返回

        if (ArrayUtils.isNotEmpty(methodProduces)) {
            Schema<?> schemaN = calculateSchema(components, returnType, jsonView, annotations);
            if (schemaN != null) {
                MediaType mediaType = new MediaType();
                mediaType.setSchema(schemaN);
                // Fill the content
                setContent(methodProduces, content, mediaType);
            }
        }
        return content;
    }

    /**
     * @see GenericResponseService#calculateSchema(Components, Type, JsonView, Annotation[])
     */
    private Schema<?> calculateSchema(Components components, Type returnType, JsonView jsonView, Annotation[] annotations) {
        // 去掉了父类的 !isVoid(returnType) 判断
        if (!SpringDocAnnotationsUtils.isAnnotationToIgnore(returnType))
            return extractSchema(components, returnType, jsonView, annotations);
        return null;
    }

    /**
     * @see GenericResponseService#setContent(String[], Content, MediaType)
     */
    private void setContent(String[] methodProduces, Content content,
                            MediaType mediaType) {
        Arrays.stream(methodProduces).forEach(mediaTypeStr -> content.addMediaType(mediaTypeStr, mediaType));
    }
}

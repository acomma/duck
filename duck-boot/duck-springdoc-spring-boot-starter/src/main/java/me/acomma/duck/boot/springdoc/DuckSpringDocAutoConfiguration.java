package me.acomma.duck.boot.springdoc;

import org.springdoc.core.parsers.ReturnTypeParser;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.service.OperationService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class DuckSpringDocAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DuckGenericResponseService duckGenericResponseService(OperationService operationService, List<ReturnTypeParser> returnTypeParsers, SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils) {
        return new DuckGenericResponseService(operationService, returnTypeParsers, springDocConfigProperties, propertyResolverUtils);
    }

    @Bean
    @ConditionalOnMissingBean
    public DuckReturnTypeParser duckReturnTypeParser() {
        return new DuckReturnTypeParser();
    }
}

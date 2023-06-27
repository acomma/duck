package me.acomma.duck.boot.jdbc;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import javax.sql.XADataSource;

/**
 * 数据源自动配置类, 自动配置顺序应在 {@link org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration} 之前.
 *
 * <p>配置示例:
 * <pre class="code">
 * duck:
 *   datasource:
 *     hikari
 *       primary:
 *         jdbc-url: jdbc:mysql://localhost:3306/duck-primary?characterEncoding=UTF-8&serverTimezone=GMT%2B8
 *         driver-class-name: com.mysql.cj.jdbc.Driver
 *         username: root
 *         password: 123456
 *       replica:
 *         jdbc-url: jdbc:mysql://localhost:3307/duck-replica?characterEncoding=UTF-8&serverTimezone=GMT%2B8
 *         driver-class-name: com.mysql.cj.jdbc.Driver
 *         username: root
 *         password: 123456
 * </pre>
 *
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
 */
@AutoConfiguration(before = {DataSourceAutoConfiguration.class})
@ConditionalOnClass({DataSource.class})
@EnableConfigurationProperties(DuckDataSourceProperties.class)
public class DuckDataSourceAutoConfiguration {
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean({DataSource.class, XADataSource.class})
    @Import({DuckDataSourceConfiguration.Hikari.class})
    protected static class DuckPooledDataSourceConfiguration {
    }

    @Bean
    public DataSourceAspect dataSourceAspect() {
        return new DataSourceAspect();
    }
}

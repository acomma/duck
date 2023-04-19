package me.acomma.duck.boot.jdbc;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源自动配置类, 自动配置顺序应在 {@link org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration} 之前.
 *
 * <p>配置示例:
 *
 * <pre class="code">
 * duck:
 *   datasource:
 *     primary:
 *       jdbc-url: jdbc:mysql://localhost:3306/duck_primary?characterEncoding=UTF-8&serverTimezone=GMT%2B8
 *       driver-class-name: com.mysql.cj.jdbc.Driver
 *       username: root
 *       password: 123456
 *     replica:
 *       jdbc-url: jdbc:mysql://localhost:3306/duck_replica?characterEncoding=UTF-8&serverTimezone=GMT%2B8
 *       driver-class-name: com.mysql.cj.jdbc.Driver
 *       username: root
 *       password: 123456
 * </pre>
 */
@Configuration
@EnableConfigurationProperties(DuckDataSourceProperties.class)
@ConditionalOnProperty(value = "duck.datasource.enable", matchIfMissing = true)
@AutoConfigureBefore({DataSourceAutoConfiguration.class})
public class DuckDataSourceAutoConfiguration {
    @Bean
    @ConfigurationProperties("duck.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("duck.datasource.replica")
    public DataSource replicaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DataSource dynamicDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.PRIMARY, primaryDataSource());
        targetDataSources.put(DataSourceType.REPLICA, replicaDataSource());
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(primaryDataSource());
        return dynamicDataSource;
    }

    @Bean
    public DataSourceAspect dataSourceAspect() {
        return new DataSourceAspect();
    }
}

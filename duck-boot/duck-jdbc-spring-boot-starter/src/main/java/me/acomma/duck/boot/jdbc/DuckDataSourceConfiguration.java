package me.acomma.duck.boot.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration
 */
abstract class DuckDataSourceConfiguration {
    @SuppressWarnings("unchecked")
    private static <T> T createDataSource(Class<? extends DataSource> type, ClassLoader classLoader) {
        return (T) DataSourceBuilder.create(classLoader)
                .type(type)
                .build();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(HikariDataSource.class)
    @ConditionalOnMissingBean(DataSource.class)
    @ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.zaxxer.hikari.HikariDataSource", matchIfMissing = true)
    static class Hikari {
        @Bean
        @ConfigurationProperties(prefix = "duck.datasource.hikari.primary")
        public HikariDataSource primaryDataSource(DuckDataSourceProperties properties) {
            return createDataSource(HikariDataSource.class, properties.getClassLoader());
        }

        @Bean
        @ConfigurationProperties(prefix = "duck.datasource.hikari.replica")
        public HikariDataSource replicaDataSource(DuckDataSourceProperties properties) {
            return createDataSource(HikariDataSource.class, properties.getClassLoader());
        }

        @Bean
        @Primary
        public DataSource dynamicDataSource(HikariDataSource primaryDataSource, HikariDataSource replicaDataSource) {
            Map<Object, Object> targetDataSources = new HashMap<>(2);
            targetDataSources.put(DataSourceType.PRIMARY, primaryDataSource);
            targetDataSources.put(DataSourceType.REPLICA, replicaDataSource);
            DynamicDataSource dynamicDataSource = new DynamicDataSource();
            dynamicDataSource.setTargetDataSources(targetDataSources);
            dynamicDataSource.setDefaultTargetDataSource(primaryDataSource);
            return dynamicDataSource;
        }
    }
}

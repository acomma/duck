package me.acomma.duck.boot.jdbc;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
 */
@ConfigurationProperties(prefix = DuckDataSourceProperties.PREFIX)
public class DuckDataSourceProperties implements BeanClassLoaderAware {
    public static final String PREFIX = "duck.datasource";
    private ClassLoader classLoader;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }
}

package me.acomma.duck.boot.jdbc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = DuckDataSourceProperties.PREFIX)
public class DuckDataSourceProperties {
    public static final String PREFIX = "duck.datasource";

    /**
     * 是否启用数据源自动配置.
     */
    private boolean enable = true;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}

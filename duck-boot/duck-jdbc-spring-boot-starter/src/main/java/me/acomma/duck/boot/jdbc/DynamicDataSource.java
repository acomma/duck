package me.acomma.duck.boot.jdbc;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        // 从当前线程中获取数据源类型作为查找数据源的 Key
        return DataSourceContext.get();
    }
}

package me.acomma.duck.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页查询的结果。
 *
 * @param <T> 分页查询返回的对象
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public class Page<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -7761205343717714975L;

    /**
     * 当前查询的页码，即当前返回的是第几页的数据。
     */
    private int pageNumber;

    /**
     * 每一页的数据量的大小。
     */
    private int pageSize;

    /**
     * 总的数据量，即当前分页查询的对象一共有多少条记录。
     */
    private long total;

    /**
     * 当前页的内容，即分页查询得到的当前页的对象的记录集合。
     */
    private List<T> content;
}

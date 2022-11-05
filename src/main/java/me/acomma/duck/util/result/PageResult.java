package me.acomma.duck.util.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * 分页结果。
 *
 * @param <T> 结果的内容的类型
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public class PageResult<T> extends BaseResult {
    @Serial
    private static final long serialVersionUID = 6519092498112720152L;

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

package me.acomma.duck.util.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 分页查询命令。
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public class PageCommand extends BaseCommand {
    @Serial
    private static final long serialVersionUID = 1589789068876302960L;

    /**
     * 当前查询的页码，即需要查询第几页的数据，默认查询第 1 页。
     */
    @Builder.Default
    private int pageNumber = 1;

    /**
     * 每一页需要查询的数据量的大小，默认每一页查询 10 条数据。
     */
    @Builder.Default
    private int pageSize = 10;
}

package me.acomma.duck.util.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * 集合结果。
 *
 * @param <T> 结果的内容的类型
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public class ListResult<T> extends BaseResult {
    @Serial
    private static final long serialVersionUID = 5050716992352066189L;

    /**
     * 结果的内容。
     */
    private List<T> content;
}

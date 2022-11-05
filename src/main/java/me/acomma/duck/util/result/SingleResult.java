package me.acomma.duck.util.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 单一结果。
 *
 * @param <T> 结果的内容的类型。
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public class SingleResult<T> extends BaseResult {
    @Serial
    private static final long serialVersionUID = -30912353453497954L;

    /**
     * 结果的内容。
     */
    private T content;
}

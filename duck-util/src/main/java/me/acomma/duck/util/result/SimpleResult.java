package me.acomma.duck.util.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 简单结果。
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public class SimpleResult extends BaseResult {
    @Serial
    private static final long serialVersionUID = 5498344796525973692L;
}

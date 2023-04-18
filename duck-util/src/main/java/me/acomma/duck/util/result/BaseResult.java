package me.acomma.duck.util.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基础结果。
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public abstract class BaseResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -8320664409496769983L;
}

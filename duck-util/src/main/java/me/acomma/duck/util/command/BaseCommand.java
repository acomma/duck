package me.acomma.duck.util.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基础命令。
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public abstract class BaseCommand implements Serializable {
    @Serial
    private static final long serialVersionUID = 4801689765257912888L;
}

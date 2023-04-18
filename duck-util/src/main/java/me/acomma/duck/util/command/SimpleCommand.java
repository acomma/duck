package me.acomma.duck.util.command;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 简单命令。
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public class SimpleCommand extends BaseCommand {
    @Serial
    private static final long serialVersionUID = -5039453053199324411L;
}

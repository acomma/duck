package me.acomma.duck.model.command.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.acomma.duck.util.command.BaseCommand;

import java.io.Serial;

/**
 * 新增角色命令。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AddRoleCommand extends BaseCommand {
    @Serial
    private static final long serialVersionUID = 249135999265237280L;

    /**
     * 角色名称。
     */
    private String name;
}

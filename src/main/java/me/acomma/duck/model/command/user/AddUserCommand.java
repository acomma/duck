package me.acomma.duck.model.command.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.acomma.duck.util.command.BaseCommand;

import java.io.Serial;

/**
 * 新增用户命令。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AddUserCommand extends BaseCommand {
    @Serial
    private static final long serialVersionUID = -8728053098642902310L;

    /**
     * 用户名。
     */
    private String username;

    /**
     * 密码。
     */
    private String password;
}

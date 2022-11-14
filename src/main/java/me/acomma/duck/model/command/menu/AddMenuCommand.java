package me.acomma.duck.model.command.menu;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.acomma.duck.util.command.BaseCommand;

import java.io.Serial;

/**
 * 新增菜单命令。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AddMenuCommand extends BaseCommand {
    @Serial
    private static final long serialVersionUID = -7907732214992720749L;

    /**
     * 上级 ID。
     */
    private Long parentId;

    /**
     * 菜单名称。
     */
    private String name;

    /**
     * 菜单路径。
     */
    private String path;
}

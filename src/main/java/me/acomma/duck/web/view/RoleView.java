package me.acomma.duck.web.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.acomma.duck.util.BaseView;

import java.io.Serial;

/**
 * 角色视图。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class RoleView extends BaseView {
    @Serial
    private static final long serialVersionUID = 3697598411461702585L;

    /**
     * 角色 ID。
     */
    private Long roleId;

    /**
     * 角色名称。
     */
    private String name;
}

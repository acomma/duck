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
public class MenuView extends BaseView {
    @Serial
    private static final long serialVersionUID = -3266398975515932996L;

    /**
     * 菜单 ID。
     */
    private Long menuId;

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

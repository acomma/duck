package me.acomma.duck.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.acomma.duck.util.BaseEntity;

import java.io.Serial;

/**
 * 角色菜单实体类。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class RoleMenuEntity extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -6594001265131900321L;

    /**
     * 角色 ID。
     */
    private Long roleId;

    /**
     * 菜单 ID。
     */
    private Long menuId;
}

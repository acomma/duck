package me.acomma.duck.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.acomma.duck.util.BaseEntity;

import java.io.Serial;

/**
 * 菜单实体类。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class MenuEntity extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 3107186607942064035L;

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

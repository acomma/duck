package me.acomma.duck.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.acomma.duck.util.BaseEntity;

import java.io.Serial;

/**
 * 角色实体类。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class RoleEntity extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -2382143108168041114L;

    /**
     * 角色 ID。
     */
    private Long roleId;

    /**
     * 角色名称。
     */
    private String name;
}

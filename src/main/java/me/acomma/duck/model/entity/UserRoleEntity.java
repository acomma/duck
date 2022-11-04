package me.acomma.duck.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.acomma.duck.util.BaseEntity;

import java.io.Serial;

/**
 * 用户角色实体类。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class UserRoleEntity extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -9158017247262085439L;

    /**
     * 用户 ID。
     */
    private Long userId;

    /**
     * 角色 ID。
     */
    private Long roleId;
}

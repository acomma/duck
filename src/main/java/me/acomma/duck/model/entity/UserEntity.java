package me.acomma.duck.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.acomma.duck.util.model.BaseEntity;

import java.io.Serial;

/**
 * 用户实体类。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class UserEntity extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -419467560420367692L;

    /**
     * 用户 ID。
     */
    private Long userId;

    /**
     * 用户名。
     */
    private String username;

    /**
     * 密码。
     */
    @ToString.Exclude
    private String password;
}

package me.acomma.duck.web.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.acomma.duck.util.BaseForm;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.util.List;

/**
 * 给用户分配角色请求参数。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AssignRoleToUserForm extends BaseForm {
    @Serial
    private static final long serialVersionUID = -6608917075270998677L;

    /**
     * 用户 ID。
     */
    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

    /**
     * 角色 ID 列表。
     */
    private List<Long> roleIds;
}

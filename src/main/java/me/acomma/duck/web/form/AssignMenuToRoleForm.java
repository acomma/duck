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
 * 给角色分配菜单请求参数。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AssignMenuToRoleForm extends BaseForm {
    @Serial
    private static final long serialVersionUID = -8644709553893583549L;

    /**
     * 角色 ID。
     */
    @NotNull(message = "角色 ID 不能为空")
    private Long roleId;

    /**
     * 菜单 ID 列表。
     */
    private List<Long> menuIds;
}

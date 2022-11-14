package me.acomma.duck.web.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.acomma.duck.util.BaseForm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;

/**
 * 新增角色请求参数。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AddRoleForm extends BaseForm {
    @Serial
    private static final long serialVersionUID = -6004312059004508291L;

    /**
     * 角色名称。
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 32, message = "角色名称长度不能超过32")
    private String name;
}

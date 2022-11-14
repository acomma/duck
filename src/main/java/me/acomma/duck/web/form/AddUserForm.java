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
 * 新增用户请求参数。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AddUserForm extends BaseForm {
    @Serial
    private static final long serialVersionUID = -3999595328645762850L;

    /**
     * 用户名。
     */
    @NotBlank(message = "用户名不能为空")
    @Size(max = 32, message = "用户名长度不能超过32")
    private String username;

    /**
     * 密码。
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能小于6")
    private String password;
}

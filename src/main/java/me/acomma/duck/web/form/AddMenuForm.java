package me.acomma.duck.web.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.acomma.duck.util.BaseForm;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;

/**
 * 新增菜单请求参数。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AddMenuForm extends BaseForm {
    @Serial
    private static final long serialVersionUID = -8677065840154125958L;

    /**
     * 上级 ID。
     */
    @NotNull(message = "上级 ID 不能为空")
    @Min(value = 0, message = "上级 ID 不能小于 0")
    private Long parentId;

    /**
     * 菜单名称。
     */
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 32, message = "菜单名称长度不能超过32")
    private String name;

    /**
     * 菜单路径。
     */
    private String path;
}

package me.acomma.duck.web.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.acomma.duck.util.BaseView;

import java.io.Serial;

/**
 * 用户视图类。
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public class UserView extends BaseView {
    @Serial
    private static final long serialVersionUID = -6577307798453751781L;

    /**
     * 用户 ID。
     */
    private Long userId;

    /**
     * 用户名。
     */
    private String username;
}

package me.acomma.duck.util;

/**
 * 定义项目中有那些模块，并给每一个模块分配一个编码。
 *
 * <p>模块编码定义为整数，取值范围为 10 ~ 99。
 */
public enum Module implements Enumerable<Integer> {
    /**
     * 用户模块。
     */
    USER(10),

    /**
     * 角色模块。
     */
    ROLE(11),

    /**
     * 菜单模块。
     */
    MENU(12);

    /**
     * 模块的编码。
     */
    private final Integer code;

    Module(Integer code) {
        this.code = code;
    }

    @Override
    public Integer value() {
        return code;
    }

    /**
     * 返回模块的编码。
     */
    public Integer code() {
        return code;
    }
}

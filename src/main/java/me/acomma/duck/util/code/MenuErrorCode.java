package me.acomma.duck.util.code;

import me.acomma.duck.util.Module;

/**
 * 菜单模块的错误码。
 */
public enum MenuErrorCode implements BusinessErrorCode {
    MENU_EXIST(1, "菜单已经存在"),

    MENU_NOT_EXIST(2, "菜单不存在"),

    PARENT_MENU_NOT_EXIST(3, "上级菜单不存在");

    /**
     * 错误编号。
     */
    private final Integer number;

    /**
     * 错误消息。
     */
    private final String message;

    MenuErrorCode(Integer number, String message) {
        this.number = number;
        this.message = message;
    }

    @Override
    public Integer value() {
        return code();
    }

    @Override
    public Integer number() {
        return number;
    }

    @Override
    public Module module() {
        return Module.MENU;
    }

    @Override
    public String message() {
        return message;
    }
}

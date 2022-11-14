package me.acomma.duck.util.code;

import me.acomma.duck.util.Module;

/**
 * 角色模块的错误码。
 */
public enum RoleErrorCode implements BusinessErrorCode {
    ROLE_EXIST(1, "角色已经存在"),

    ROLE_NOT_EXIST(2, "角色不存在");

    /**
     * 错误编号。
     */
    private final Integer number;

    /**
     * 错误消息。
     */
    private final String message;

    RoleErrorCode(Integer number, String message) {
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
        return Module.ROLE;
    }

    @Override
    public String message() {
        return message;
    }
}

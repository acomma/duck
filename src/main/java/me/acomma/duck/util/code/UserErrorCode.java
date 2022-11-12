package me.acomma.duck.util.code;

import me.acomma.duck.util.Module;

/**
 * 用户模块的错误码。
 */
public enum UserErrorCode implements BusinessErrorCode {
    USER_EXIST(1, "用户已经存在");

    /**
     * 错误编号。
     */
    private final Integer number;

    /**
     * 错误消息。
     */
    private final String message;

    UserErrorCode(Integer number, String message) {
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
        return Module.USER;
    }

    @Override
    public String message() {
        return message;
    }
}

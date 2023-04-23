package me.acomma.duck.util;

/**
 * 定义项目中有那些模块，并给每一个模块分配一个编码。
 *
 * <p>模块编码定义为整数，取值范围为 10 ~ 99，作为业务错误码 {@link me.acomma.duck.util.code.BusinessErrorCode} 的前两位。
 */
public interface Module extends Enumerable<Integer> {
    @Override
    default Integer value() {
        return code();
    }

    /**
     * 返回模块的编码。
     */
    Integer code();
}

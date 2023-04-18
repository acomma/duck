package me.acomma.duck.util.code;

import me.acomma.duck.util.Module;

/**
 * 业务错误码定义为整数，由 2 部分组成：模块编码（2位） + 错误编号（3位）。模块编码定义在 {@link Module} 类中；错误编号定义为整数，取值范围为 1 ~ 999。
 * 举个例子，模块编码为 35，错误编号为 78，则模块编码为 35078。
 *
 * <p>注意：这里的错误码是针对单体系统进行定义的，如果是是由多个项目组成的分布式系统，则可以在当前错误码定义的基础上加上项目编码得到新的错误码定义，比如：项目编码（2位） + 模块编码（2位） + 错误编号（3位）。
 *
 * <p>备注：错误编号在百度翻译、有道翻译和微软翻译三个翻译软件中都是 <i>error number</i>，为了简介一点只保留了 <i>number</i>。
 * 曾经考虑过 <i>ordinal</i> 和 <i>serial</i>，<i>ordinal</i> 在枚举类中和 {@link Enum#ordinal()} 冲突而放弃；<i>serial</i> 语义与要表达的意思不太符合而放弃。
 */
public interface BusinessErrorCode extends ErrorCode<Integer> {
    /**
     * @return 错误编号
     */
    Integer number();

    /**
     * @return 错误码所属的模块
     */
    Module module();

    /**
     * @return 业务错误码
     */
    default Integer code() {
        return module().code() * 1000 + number();
    }
}

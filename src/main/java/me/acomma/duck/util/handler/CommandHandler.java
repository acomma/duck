package me.acomma.duck.util.handler;

import me.acomma.duck.util.command.BaseCommand;
import me.acomma.duck.util.result.BaseResult;

/**
 * 命令处理器。
 *
 * @param <C> 命令的类型
 * @param <R> 结果的类型
 */
public interface CommandHandler<C extends BaseCommand, R extends BaseResult> {
    /**
     * 处理命令。
     *
     * @param command 命令
     * @return 命令处理结果
     */
    R handle(C command);
}

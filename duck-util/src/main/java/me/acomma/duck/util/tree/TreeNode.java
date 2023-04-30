package me.acomma.duck.util.tree;

import java.util.List;

/**
 * 树节点，要使用 {@code TreeUtil} 实现把列表构建为树形结构的类需要实现该接口。参考 <a href="https://gitee.com/objs/mayfly">Coder慌 / mayfly</a> 中的 {@code TreeUtils} 类。
 *
 * @param <I> 节点的 ID 的类型
 * @param <T> 节点的类型
 */
public interface TreeNode<I, T extends TreeNode<I, T>> {
    /**
     * 返回节点的 ID
     */
    I id();

    /**
     * 返回节点的上级 ID
     */
    I parentId();

    /**
     * 返回是否根节点
     */
    boolean root();

    /**
     * 返回节点的下级节点
     */
    List<T> children();

    /**
     * 设置节点的下级节点
     */
    void children(List<T> children);
}

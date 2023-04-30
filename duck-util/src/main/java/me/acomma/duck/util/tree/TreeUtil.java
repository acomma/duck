package me.acomma.duck.util.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 树结构工具类。
 */
public class TreeUtil {
    /**
     * 将列表结构构建为树形结构。
     *
     * @param list 待构建为树形结构的列表，列表中的元素需要实现 {@code TreeNode} 接口
     * @param <I>  节点 ID 的类型
     * @param <T>  节点的类型
     * @return 树形结构
     */
    public static <I, T extends TreeNode<I, T>> List<T> toTree(List<T> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> roots = new ArrayList<>();
        Map<I, T> map = list.stream().collect(Collectors.toMap(T::id, Function.identity()));

        for (T t : list) {
            if (t.root()) {
                roots.add(t);
            } else {
                T parent = map.get(t.parentId());
                if (parent.children() == null) {
                    parent.children(new ArrayList<>());
                }
                parent.children().add(t);
            }
        }

        return roots;
    }

    /**
     * 将列表结构构建为树形结构。参考 <a href="https://gitee.com/xiaokedamowang/bilibili">小可大魔王 / bilibili</a> 中的 {@code ListToTreeUtil} 类。
     *
     * @param list            待构建为树形结构的列表
     * @param isRootFunc      判断是否根节点的函数
     * @param getIdFunc       获取节点 ID 的函数
     * @param getParentIdFunc 获取节点的上级节点的 ID 的函数
     * @param getChildrenFunc 获取节点的下级节点的函数
     * @param setChildrenFunc 设置节点的下级节点的函数
     * @param <T>             节点的类型
     * @return 树形结构
     */
    public static <T> List<T> toTree(List<T> list, Predicate<T> isRootFunc,
                                     Function<T, ?> getIdFunc, Function<T, ?> getParentIdFunc,
                                     Function<T, List<T>> getChildrenFunc, BiConsumer<T, List<T>> setChildrenFunc) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> roots = new ArrayList<>();
        Map<Object, T> map = list.stream().collect(Collectors.toMap(getIdFunc, Function.identity()));

        for (T t : list) {
            if (isRootFunc.test(t)) {
                roots.add(t);
            } else {
                T parent = map.get(getParentIdFunc.apply(t));
                if (getChildrenFunc.apply(parent) == null) {
                    setChildrenFunc.accept(parent, new ArrayList<>());
                }
                getChildrenFunc.apply(parent).add(t);
            }
        }

        return roots;
    }

    /**
     * 将列表结构构建为树形结构。
     *
     * @param list            待构建为树形结构的列表
     * @param isRootFunc      判断是否根节点的函数
     * @param getIdFunc       获取节点 ID 的函数
     * @param getParentIdFunc 获取节点的上级节点的 ID 的函数
     * @param getChildrenFunc 获取节点的下级节点的函数
     * @param setChildrenFunc 设置节点的下级节点的函数
     * @param convertor       将列表节点转换为树形节点
     * @param <T>             列表节点的类型
     * @param <R>             树形节点的类型
     * @return 树形结构
     */
    public static <T, R> List<R> toTree(List<T> list, Predicate<R> isRootFunc,
                                        Function<R, ?> getIdFunc, Function<R, ?> getParentIdFunc,
                                        Function<R, List<R>> getChildrenFunc, BiConsumer<R, List<R>> setChildrenFunc,
                                        Function<T, R> convertor) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        List<R> rs = new ArrayList<>();
        Map<Object, R> map = new HashMap<>();

        for (T t : list) {
            R r = convertor.apply(t);
            rs.add(r);
            map.put(getIdFunc.apply(r), r);
        }

        List<R> roots = new ArrayList<>();

        for (R r : rs) {
            if (isRootFunc.test(r)) {
                roots.add(r);
            } else {
                R parent = map.get(getParentIdFunc.apply(r));
                if (getChildrenFunc.apply(parent) == null) {
                    setChildrenFunc.accept(parent, new ArrayList<>());
                }
                getChildrenFunc.apply(parent).add(r);
            }
        }

        return roots;
    }

    /**
     * 使用广度优先算法将树形结构转换为列表结构。
     *
     * @param tree 待转换为列表的树形结构
     * @param <I>  节点 ID 的类型
     * @param <T>  节点的类型
     * @return 列表结构
     */
    public static <I, T extends TreeNode<I, T>> List<T> toList(List<T> tree) {
        if (tree == null || tree.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> list = new ArrayList<>();

        Queue<T> queue = new LinkedList<>(tree);

        while (!queue.isEmpty()) {
            T t = queue.remove();

            List<T> children = t.children();
            if (children != null && !children.isEmpty()) {
                queue.addAll(children);
            }

            t.children(null);

            list.add(t);
        }

        return list;
    }

    /**
     * 使用广度优先算法将树形结构转换为列表结构。
     *
     * @param tree            待转换为列表的树形结构
     * @param getChildrenFunc 获取节点的下级节点的函数
     * @param setChildrenFunc 设置节点的下级节点的函数
     * @param <T>             节点的类型
     * @return 列表结构
     */
    public static <T> List<T> toList(List<T> tree, Function<T, List<T>> getChildrenFunc, BiConsumer<T, List<T>> setChildrenFunc) {
        if (tree == null || tree.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> list = new ArrayList<>();

        Queue<T> queue = new LinkedList<>(tree);

        while (!queue.isEmpty()) {
            T t = queue.remove();

            List<T> children = getChildrenFunc.apply(t);
            if (children != null && !children.isEmpty()) {
                queue.addAll(children);
            }

            setChildrenFunc.accept(t, null);

            list.add(t);
        }

        return list;
    }

    /**
     * 使用广度优先算法将树形结构转换为列表结构。
     *
     * @param tree            待转换为列表的树形结构
     * @param getChildrenFunc 获取节点的下级节点的函数
     * @param convertor       将树形节点转换为列表节点
     * @param <T>             树形节点的类型
     * @param <R>             列表节点的类型
     * @return 列表结构
     */
    public static <T, R> List<R> toList(List<T> tree, Function<T, List<T>> getChildrenFunc, Function<T, R> convertor) {
        if (tree == null || tree.isEmpty()) {
            return new ArrayList<>();
        }

        List<R> list = new ArrayList<>();

        Queue<T> queue = new LinkedList<>(tree);

        while (!queue.isEmpty()) {
            T t = queue.remove();

            List<T> children = getChildrenFunc.apply(t);
            if (children != null && !children.isEmpty()) {
                queue.addAll(children);
            }

            R r = convertor.apply(t);

            list.add(r);
        }

        return list;
    }
}

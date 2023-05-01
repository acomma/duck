package me.acomma.duck.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 一个类实现了 {@code Constant} 接口就表示它是一个常量类.
 */
public interface Constant extends Serializable {
    /**
     * 查询所有的常量值.
     *
     * @return 常量值集合
     */
    List<Integer> list();

    /**
     * 查询所有的常量值及常量值的描述.
     *
     * @return 常量值集合
     */
    Map<Integer, String> map();

    /**
     * 如果常量类中存在指定的常量值, 则返回 <tt>true</tt>.
     *
     * @param c 要测试其在此常量类中中的存在的常量值
     * @return 如果常量类中存在指定的常量值, 则返回 <tt>true</tt>
     */
    boolean contains(Integer c);

    /**
     * 返回指定的常量值的描述.
     *
     * @param c 要返回常量值描述的常量值
     * @return 指定的常量值的描述
     */
    String description(Integer c);
}

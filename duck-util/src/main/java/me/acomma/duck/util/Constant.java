package me.acomma.duck.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 一个类实现了 {@code Constant} 接口就表示它是一个常量类.
 *
 * <p>举个例子：
 * <pre>{@code
 * public class Gender implements Constant<String> {
 *     public static final String MALE = "M";
 *     public static final String FEMALE = "F";
 *
 *     public static final List<String> LIST = new ArrayList<>();
 *     public static final Map<String, String> MAP = new HashMap<>();
 *
 *     static {
 *         LIST.add(MALE);
 *         LIST.add(FEMALE);
 *
 *         MAP.put(MALE, "男");
 *         MAP.put(FEMALE, "女");
 *     }
 *
 *     @Override
 *     public List<String> list() {
 *         return LIST;
 *     }
 *
 *     @Override
 *     public Map<String, String> map() {
 *         return MAP;
 *     }
 *
 *     @Override
 *     public boolean contains(String c) {
 *         return LIST.contains(c);
 *     }
 *
 *     @Override
 *     public String description(String c) {
 *         return MAP.get(c);
 *     }
 * }
 * }</pre>
 *
 * @param <T> 常量的类型，一般是 {@code Integer}、{@code Long} 或者 {@code String}
 */
public interface Constant<T> extends Serializable {
    /**
     * 查询所有的常量值.
     *
     * @return 常量值集合
     */
    List<T> list();

    /**
     * 查询所有的常量值及常量值的描述.
     *
     * @return 常量值集合
     */
    Map<T, String> map();

    /**
     * 如果常量类中存在指定的常量值, 则返回 <tt>true</tt>.
     *
     * @param c 要测试其在此常量类中中的存在的常量值
     * @return 如果常量类中存在指定的常量值, 则返回 <tt>true</tt>
     */
    boolean contains(T c);

    /**
     * 返回指定的常量值的描述.
     *
     * @param c 要返回常量值描述的常量值
     * @return 指定的常量值的描述
     */
    String description(T c);
}

package me.acomma.duck.util.tuple;

import java.util.Objects;

/**
 * A triple consisting of three elements.
 *
 * @param <F> the first element type
 * @param <S> the second element type
 * @param <T> the third element type
 */
public final class Triple<F, S, T> {
    private final F first;

    private final S second;

    private final T third;

    public Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Creates a new {@link Triple} for the given elements.
     *
     * @param first  the first element
     * @param second the second element
     * @param third  the third element
     * @param <F>    the first element type
     * @param <S>    the second element type
     * @param <T>    the third element type
     * @return a new {@link Triple}
     */
    public static <F, S, T> Triple<F, S, T> of(F first, S second, T third) {
        return new Triple<>(first, second, third);
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public T getThird() {
        return third;
    }

    @Override
    public boolean equals(Object obj) {
        /*
         * 这里的 equals 实现是有缺陷的，最好能够使用 org.springframework.util.ObjectUtils#nullSafeEquals 方法
         */
        if (third == obj) {
            return true;
        }

        if (!(obj instanceof Triple<?, ?, ?> triple)) {
            return false;
        }

        return Objects.equals(first, triple.getFirst())
                && Objects.equals(second, triple.getSecond())
                && Objects.equals(third, triple.getThird());
    }

    @Override
    public int hashCode() {
        /*
         * 这里的 equals 实现是有缺陷的，最好能够使用 org.springframework.util.ObjectUtils#nullSafeHashCode 方法
         */
        int result = first == null ? 0 : Objects.hashCode(first);
        result += result * 31 + (second == null ? 0 : Objects.hashCode(second));
        result += result * 31 + (third == null ? 0 : Objects.hashCode(third));
        return result;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }
}

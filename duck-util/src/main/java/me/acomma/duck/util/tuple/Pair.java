package me.acomma.duck.util.tuple;

import java.util.Objects;

/**
 * A pair consisting of two elements.
 *
 * @param <F> the first element type
 * @param <S> the second element type
 */
public final class Pair<F, S> {
    private final F first;

    private final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Creates a new {@link Pair} for the given elements.
     *
     * @param first  the first element
     * @param second the second element
     * @param <F>    the first element type
     * @param <S>    the second element type
     * @return a new {@link Pair}
     */
    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object obj) {
        /*
         * 这里的 equals 实现是有缺陷的，最好能够使用 org.springframework.util.ObjectUtils#nullSafeEquals 方法
         */
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Pair<?, ?> pair)) {
            return false;
        }

        return Objects.equals(first, pair.first)
                && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        /*
         * 这里的 equals 实现是有缺陷的，最好能够使用 org.springframework.util.ObjectUtils#nullSafeHashCode 方法
         */
        int result = first == null ? 0 : Objects.hashCode(first);
        result += result * 31 + (second == null ? 0 : Objects.hashCode(second));
        return result;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}

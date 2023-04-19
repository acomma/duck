package me.acomma.duck.boot.cache.expression;

import org.springframework.expression.EvaluationException;

/**
 * Copy from {@link org.springframework.cache.interceptor.VariableNotAvailableException}.
 */
@SuppressWarnings("serial")
public class VariableNotAvailableException extends EvaluationException {
    private final String name;

    public VariableNotAvailableException(String name) {
        super("Variable not available");
        this.name = name;
    }

    public final String getName() {
        return this.name;
    }
}

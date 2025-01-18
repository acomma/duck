package me.acomma.duck.util;

import me.acomma.duck.util.code.BusinessErrorCode;
import me.acomma.duck.util.exception.BusinessException;

import java.util.Collection;
import java.util.Map;

public abstract class Assert {
    public static void isTrue(boolean expression, BusinessErrorCode code) {
        if (!expression) {
            throw new BusinessException(code);
        }
    }

    public static void isTrue(boolean expression, BusinessErrorCode code, Object... arguments) {
        if (!expression) {
            throw new BusinessException(code, arguments);
        }
    }

    public static void isTrue(boolean expression, BusinessErrorCode code, String message) {
        if (!expression) {
            throw new BusinessException(code, message);
        }
    }

    public static void isTrue(boolean expression, BusinessErrorCode code, String message, Object... arguments) {
        if (!expression) {
            throw new BusinessException(code, message, arguments);
        }
    }

    public static void isFalse(boolean expression, BusinessErrorCode code) {
        if (expression) {
            throw new BusinessException(code);
        }
    }

    public static void isFalse(boolean expression, BusinessErrorCode code, Object... arguments) {
        if (expression) {
            throw new BusinessException(code, arguments);
        }
    }

    public static void isFalse(boolean expression, BusinessErrorCode code, String message) {
        if (expression) {
            throw new BusinessException(code, message);
        }
    }

    public static void isFalse(boolean expression, BusinessErrorCode code, String message, Object... arguments) {
        if (expression) {
            throw new BusinessException(code, message, arguments);
        }
    }

    public static void isNull(Object object, BusinessErrorCode code) {
        if (object != null) {
            throw new BusinessException(code);
        }
    }

    public static void isNull(Object object, BusinessErrorCode code, Object... arguments) {
        if (object != null) {
            throw new BusinessException(code, arguments);
        }
    }

    public static void isNull(Object object, BusinessErrorCode code, String message) {
        if (object != null) {
            throw new BusinessException(code, message);
        }
    }

    public static void isNull(Object object, BusinessErrorCode code, String message, Object... arguments) {
        if (object != null) {
            throw new BusinessException(code, message, arguments);
        }
    }

    public static void notNull(Object object, BusinessErrorCode code) {
        if (object == null) {
            throw new BusinessException(code);
        }
    }

    public static void notNull(Object object, BusinessErrorCode code, Object... arguments) {
        if (object == null) {
            throw new BusinessException(code, arguments);
        }
    }

    public static void notNull(Object object, BusinessErrorCode code, String message) {
        if (object == null) {
            throw new BusinessException(code, message);
        }
    }

    public static void notNull(Object object, BusinessErrorCode code, String message, Object... arguments) {
        if (object == null) {
            throw new BusinessException(code, message, arguments);
        }
    }

    public static void isEmpty(Object[] array, BusinessErrorCode code) {
        if (array != null && array.length != 0) {
            throw new BusinessException(code);
        }
    }

    public static void isEmpty(Object[] array, BusinessErrorCode code, Object... arguments) {
        if (array != null && array.length != 0) {
            throw new BusinessException(code, arguments);
        }
    }

    public static void isEmpty(Object[] array, BusinessErrorCode code, String message) {
        if (array != null && array.length != 0) {
            throw new BusinessException(code, message);
        }
    }

    public static void isEmpty(Object[] array, BusinessErrorCode code, String message, Object... arguments) {
        if (array != null && array.length != 0) {
            throw new BusinessException(code, message, arguments);
        }
    }

    public static void notEmpty(Object[] array, BusinessErrorCode code) {
        if (array == null || array.length == 0) {
            throw new BusinessException(code);
        }
    }

    public static void notEmpty(Object[] array, BusinessErrorCode code, Object... arguments) {
        if (array == null || array.length == 0) {
            throw new BusinessException(code, arguments);
        }
    }

    public static void notEmpty(Object[] array, BusinessErrorCode code, String message) {
        if (array == null || array.length == 0) {
            throw new BusinessException(code, message);
        }
    }

    public static void notEmpty(Object[] array, BusinessErrorCode code, String message, Object... arguments) {
        if (array == null || array.length == 0) {
            throw new BusinessException(code, message, arguments);
        }
    }

    public static void isEmpty(Collection<?> collection, BusinessErrorCode code) {
        if (collection != null && !collection.isEmpty()) {
            throw new BusinessException(code);
        }
    }

    public static void isEmpty(Collection<?> collection, BusinessErrorCode code, Object... arguments) {
        if (collection != null && !collection.isEmpty()) {
            throw new BusinessException(code, arguments);
        }
    }

    public static void isEmpty(Collection<?> collection, BusinessErrorCode code, String message) {
        if (collection != null && !collection.isEmpty()) {
            throw new BusinessException(code, message);
        }
    }

    public static void isEmpty(Collection<?> collection, BusinessErrorCode code, String message, Object... arguments) {
        if (collection != null && !collection.isEmpty()) {
            throw new BusinessException(code, message, arguments);
        }
    }

    public static void notEmpty(Collection<?> collection, BusinessErrorCode code) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(code);
        }
    }

    public static void notEmpty(Collection<?> collection, BusinessErrorCode code, Object... arguments) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(code, arguments);
        }
    }

    public static void notEmpty(Collection<?> collection, BusinessErrorCode code, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(code, message);
        }
    }

    public static void notEmpty(Collection<?> collection, BusinessErrorCode code, String message, Object... arguments) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(code, message, arguments);
        }
    }

    public static void isEmpty(Map<?, ?> map, BusinessErrorCode code) {
        if (map != null && !map.isEmpty()) {
            throw new BusinessException(code);
        }
    }

    public static void isEmpty(Map<?, ?> map, BusinessErrorCode code, Object... arguments) {
        if (map != null && !map.isEmpty()) {
            throw new BusinessException(code, arguments);
        }
    }

    public static void isEmpty(Map<?, ?> map, BusinessErrorCode code, String message) {
        if (map != null && !map.isEmpty()) {
            throw new BusinessException(code, message);
        }
    }

    public static void isEmpty(Map<?, ?> map, BusinessErrorCode code, String message, Object... arguments) {
        if (map != null && !map.isEmpty()) {
            throw new BusinessException(code, message, arguments);
        }
    }

    public static void notEmpty(Map<?, ?> map, BusinessErrorCode code) {
        if (map == null || map.isEmpty()) {
            throw new BusinessException(code);
        }
    }

    public static void notEmpty(Map<?, ?> map, BusinessErrorCode code, Object... arguments) {
        if (map == null || map.isEmpty()) {
            throw new BusinessException(code, arguments);
        }
    }

    public static void notEmpty(Map<?, ?> map, BusinessErrorCode code, String message) {
        if (map == null || map.isEmpty()) {
            throw new BusinessException(code, message);
        }
    }

    public static void notEmpty(Map<?, ?> map, BusinessErrorCode code, String message, Object... arguments) {
        if (map == null || map.isEmpty()) {
            throw new BusinessException(code, message, arguments);
        }
    }
}

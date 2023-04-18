package me.acomma.duck.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基础表单类。包装外部系统的请求参数的类需要继承该类。
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public abstract class BaseForm implements Serializable {
    @Serial
    private static final long serialVersionUID = 6712875633530844692L;
}

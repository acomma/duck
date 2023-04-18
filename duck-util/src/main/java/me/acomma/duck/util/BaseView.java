package me.acomma.duck.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基础视图类。包装返回给外部系统的响应参数的类需要继承该类。
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public abstract class BaseView implements Serializable {
    @Serial
    private static final long serialVersionUID = 4585180695206994514L;
}

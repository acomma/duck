package me.acomma.duck.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基本实体类。大多数实体都应该继承该类，少数关系表或者中间表是否需要继承该类应该根据实际情况选择。
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1452713298550009562L;

    /**
     * ID。表示数据库的主键，是数据库记录的唯一标识，由数据库自动生成，步长为 1。
     */
    private Long id;

    /**
     * 创建时间。
     */
    private LocalDateTime createTime;

    /**
     * 更新时间。
     */
    private LocalDateTime updateTime;
}

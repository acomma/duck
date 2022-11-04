package me.acomma.duck.mapper;

import me.acomma.duck.model.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户数据访问对象。
 */
@Mapper
public interface UserMapper {
    /**
     * 新增用户。
     *
     * @param user 用户实体类
     * @return 影响记录数
     */
    int insert(UserEntity user);

    /**
     * 根据用户 ID 删除用户。
     *
     * @param userId 用户 ID
     * @return 影响记录数
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据用户 ID 更新用户密码。
     *
     * @param userId   用户 ID
     * @param password 密码
     * @return 影响记录数
     */
    int updatePasswordByUserId(@Param("userId") Long userId, @Param("password") String password);

    /**
     * 根据用户 ID 查询用户。
     *
     * @param userId 用户 ID
     * @return 用户实体类
     */
    UserEntity findByUserId(@Param("userId") Long userId);

    /**
     * 根据用户名查询用户。
     *
     * @param username 用户名
     * @return 用户实体类
     */
    UserEntity findByUsername(@Param("username") String username);
}

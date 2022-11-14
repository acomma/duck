package me.acomma.duck.mapper;

import me.acomma.duck.model.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色数据访问对象。
 */
@Mapper
public interface UserRoleMapper {
    /**
     * 新增用户的角色列表。
     *
     * @param userId  用户 ID
     * @param roleIds 角色 ID 列表
     * @return
     */
    int insertByUserIdAndRoleIds(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    /**
     * 删除用户的角色。
     *
     * @param userId  待删除角色的用户 ID
     * @param roleIds 待删除的用户的角色 ID 列表
     * @return 删除的记录数
     */
    int removeByUserIdAndRoleIds(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    /**
     * 查询用户的角色列表。
     *
     * @param userId 用户 ID
     * @return 用户的角色列表
     */
    List<UserRoleEntity> listByUserId(@Param("userId") Long userId);
}

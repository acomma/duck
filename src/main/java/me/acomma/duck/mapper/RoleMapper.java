package me.acomma.duck.mapper;

import me.acomma.duck.model.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色数据访问对象。
 */
@Mapper
public interface RoleMapper {
    /**
     * 新增角色。
     *
     * @param role 角色实体类
     * @return 影响记录数
     */
    int insert(RoleEntity role);

    /**
     * 根据角色 ID 删除角色。
     *
     * @param roleId 角色 ID
     * @return 影响记录数
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色 ID 更新角色名称。
     *
     * @param roleId 角色 ID
     * @param name   角色名称
     * @return 影响记录数
     */
    int updateNameByRoleId(@Param("roleId") Long roleId, @Param("name") String name);

    /**
     * 根据角色 ID 查询角色。
     *
     * @param roleId 角色 ID
     * @return 角色实体类
     */
    RoleEntity findByRoleId(@Param("roleId") Long roleId);
}

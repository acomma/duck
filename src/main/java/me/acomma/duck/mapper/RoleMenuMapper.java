package me.acomma.duck.mapper;

import me.acomma.duck.model.entity.RoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色菜单数据访问对象。
 */
@Mapper
public interface RoleMenuMapper {
    /**
     * 新增角色的菜单列表。
     *
     * @param roleId  角色 ID
     * @param menuIds 菜单 ID 列表
     * @return 新增的记录数
     */
    int insertByRoleIdAndMenuIds(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);

    /**
     * 删除角色的菜单。
     *
     * @param roleId  待删除菜单的角色 ID
     * @param menuIds 待删除的角色的菜单 ID 列表
     * @return 删除的记录数
     */
    int removeByRoleIdAndMenuIds(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);

    /**
     * 查询角色的菜单列表。
     *
     * @param roleId 菜单 ID
     * @return 角色的菜单列表
     */
    List<RoleMenuEntity> listByRoleId(@Param("roleId") Long roleId);
}

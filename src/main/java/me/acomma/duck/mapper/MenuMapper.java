package me.acomma.duck.mapper;

import me.acomma.duck.model.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单数据访问对象。
 */
@Mapper
public interface MenuMapper {
    /**
     * 新增菜单。
     *
     * @param menu 待新增的菜单
     * @return 影响记录数
     */
    int insert(MenuEntity menu);

    /**
     * 根据菜单 ID 删除菜单。
     *
     * @param menuId 菜单 ID
     * @return 影响记录数
     */
    int deleteByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据菜单 ID 更新菜单。
     *
     * @param menu 待更新的菜单
     * @return 影响记录数
     */
    int updateByMenuId(MenuEntity menu);

    /**
     * 根据菜单 ID 查询菜单。
     *
     * @param menuId 菜单 ID
     * @return 菜单实体类
     */
    MenuEntity findByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据上级 ID 和菜单名称查询菜单。
     *
     * @param parentId 菜单 ID
     * @param name     菜单名称
     * @return 菜单
     */
    MenuEntity findByParentIdAndName(@Param("parentId") Long parentId, @Param("name") String name);

    /**
     * 根据菜单 ID 列表查询菜单。
     *
     * @param menuIds 菜单 ID 列表
     * @return 菜单列表
     */
    List<MenuEntity> listByMenuIds(@Param("menuIds") List<Long> menuIds);
}

package me.acomma.duck.mapper;

import me.acomma.duck.model.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}

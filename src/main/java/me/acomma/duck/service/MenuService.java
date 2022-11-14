package me.acomma.duck.service;

import me.acomma.duck.mapper.MenuMapper;
import me.acomma.duck.model.command.menu.AddMenuCommand;
import me.acomma.duck.model.entity.MenuEntity;
import me.acomma.duck.util.code.MenuErrorCode;
import me.acomma.duck.util.exception.BusinessException;
import me.acomma.duck.util.id.ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 菜单服务。
 */
@Service
public class MenuService {
    @Autowired
    private MenuMapper menuMapper;

    /**
     * 新增菜单。
     *
     * @param command 新增菜单命令
     * @return 新菜单的菜单 ID
     */
    @Transactional(rollbackFor = Throwable.class)
    public Long addMenu(AddMenuCommand command) {
        if (!Objects.equals(command.getParentId(), 0L)) {
            MenuEntity existing = menuMapper.findByMenuId(command.getParentId());
            if (Objects.nonNull(existing)) {
                throw new BusinessException(MenuErrorCode.PARENT_MENU_NOT_EXIST);
            }
        }

        MenuEntity existing = menuMapper.findByParentIdAndName(command.getParentId(), command.getName());
        if (Objects.nonNull(existing)) {
            throw new BusinessException(MenuErrorCode.MENU_EXIST);
        }

        MenuEntity menu = new MenuEntity();
        menu.setMenuId(ID.nextId());
        menu.setParentId(command.getParentId());
        menu.setName(command.getName());
        menu.setPath(command.getPath());

        menuMapper.insert(menu);

        return menu.getMenuId();
    }

    /**
     * 获取菜单。
     *
     * @param roleId 菜单 ID
     * @return 菜单
     */
    public MenuEntity getByMenuId(Long roleId) {
        MenuEntity menu = menuMapper.findByMenuId(roleId);
        if (Objects.isNull(menu)) {
            throw new BusinessException(MenuErrorCode.MENU_NOT_EXIST);
        }
        return menu;
    }
}

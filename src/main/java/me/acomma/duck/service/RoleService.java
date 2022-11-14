package me.acomma.duck.service;

import me.acomma.duck.mapper.MenuMapper;
import me.acomma.duck.mapper.RoleMapper;
import me.acomma.duck.mapper.RoleMenuMapper;
import me.acomma.duck.model.command.role.AddRoleCommand;
import me.acomma.duck.model.entity.MenuEntity;
import me.acomma.duck.model.entity.RoleEntity;
import me.acomma.duck.model.entity.RoleMenuEntity;
import me.acomma.duck.util.code.RoleErrorCode;
import me.acomma.duck.util.exception.BusinessException;
import me.acomma.duck.util.id.ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 角色服务。
 */
@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * 新增角色。
     *
     * @param command 新增角色命令
     * @return 新角色的角色 ID
     */
    @Transactional(rollbackFor = Throwable.class)
    public Long addRole(AddRoleCommand command) {
        RoleEntity existing = roleMapper.findByName(command.getName());
        if (Objects.nonNull(existing)) {
            throw new BusinessException(RoleErrorCode.ROLE_EXIST);
        }

        RoleEntity role = new RoleEntity();
        role.setRoleId(ID.nextId());
        role.setName(command.getName());

        roleMapper.insert(role);

        return role.getRoleId();
    }

    /**
     * 给角色分配菜单。
     *
     * @param roleId  待分配菜单的角色 ID
     * @param menuIds 即将给角色分配的菜单 ID 列表
     */
    @Transactional(rollbackFor = Throwable.class)
    public void assignMenuToRole(Long roleId, List<Long> menuIds) {
        RoleEntity existing = roleMapper.findByRoleId(roleId);
        if (Objects.isNull(existing)) {
            throw new BusinessException(RoleErrorCode.ROLE_NOT_EXIST);
        }

        List<Long> validMenuIds = getValidMenuIds(menuIds);

        List<RoleMenuEntity> existingRoleMenus = roleMenuMapper.listByRoleId(roleId);
        List<Long> existingMenuIds = existingRoleMenus.stream().map(RoleMenuEntity::getMenuId).toList();

        List<Long> needRemoveMenuIds = existingMenuIds.stream().filter(e -> !validMenuIds.contains(e)).toList();
        if (!CollectionUtils.isEmpty(needRemoveMenuIds)) {
            roleMenuMapper.removeByRoleIdAndMenuIds(roleId, needRemoveMenuIds);
        }

        List<Long> needAddMenuIds = validMenuIds.stream().filter(e -> !existingMenuIds.contains(e)).toList();
        if (!CollectionUtils.isEmpty(needAddMenuIds)) {
            roleMenuMapper.insertByRoleIdAndMenuIds(roleId, needAddMenuIds);
        }
    }

    /**
     * 获取有效的菜单 ID 列表。
     *
     * @param menuIds 菜单 ID 列表，可以包含无效的菜单 ID
     * @return 有效的菜单 ID 列表
     */
    private List<Long> getValidMenuIds(List<Long> menuIds) {
        if (Objects.isNull(menuIds)) {
            return Collections.emptyList();
        }
        List<MenuEntity> menus = menuMapper.listByMenuIds(menuIds);
        return menus.stream().map(MenuEntity::getMenuId).toList();
    }

    /**
     * 获取角色。
     *
     * @param roleId 角色 ID
     * @return 角色
     */
    public RoleEntity getByRoleId(Long roleId) {
        RoleEntity role = roleMapper.findByRoleId(roleId);
        if (Objects.isNull(role)) {
            throw new BusinessException(RoleErrorCode.ROLE_NOT_EXIST);
        }
        return role;
    }
}

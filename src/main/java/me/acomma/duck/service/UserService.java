package me.acomma.duck.service;

import me.acomma.duck.mapper.RoleMapper;
import me.acomma.duck.mapper.UserMapper;
import me.acomma.duck.mapper.UserRoleMapper;
import me.acomma.duck.model.command.user.AddUserCommand;
import me.acomma.duck.model.entity.RoleEntity;
import me.acomma.duck.model.entity.UserEntity;
import me.acomma.duck.model.entity.UserRoleEntity;
import me.acomma.duck.util.code.UserErrorCode;
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
 * 用户服务。
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 新增用户。
     *
     * @param command 新增用户命令
     * @return 新用户的用户 ID
     */
    @Transactional(rollbackFor = Throwable.class)
    public Long addUser(AddUserCommand command) {
        UserEntity existing = userMapper.findByUsername(command.getUsername());
        if (Objects.nonNull(existing)) {
            throw new BusinessException(UserErrorCode.USER_EXIST);
        }

        UserEntity user = new UserEntity();
        user.setUserId(ID.nextId());
        user.setUsername(command.getUsername());
        user.setPassword(command.getPassword());

        userMapper.insert(user);

        return user.getUserId();
    }

    /**
     * 给用户分配角色。
     *
     * @param userId  待分配角色的用户 ID
     * @param roleIds 即将给用户分配的角色 ID 列表
     */
    @Transactional(rollbackFor = Throwable.class)
    public void assignRoleToUser(Long userId, List<Long> roleIds) {
        UserEntity existing = userMapper.findByUserId(userId);
        if (Objects.isNull(existing)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }

        List<Long> validRoleIds = getValidRoleIds(roleIds);

        List<UserRoleEntity> existingUserRoles = userRoleMapper.listByUserId(userId);
        List<Long> existingRoleIds = existingUserRoles.stream().map(UserRoleEntity::getRoleId).toList();

        List<Long> needRemoveRoleIds = existingRoleIds.stream().filter(e -> !validRoleIds.contains(e)).toList();
        if (!CollectionUtils.isEmpty(needRemoveRoleIds)) {
            userRoleMapper.removeByUserIdAndRoleIds(userId, needRemoveRoleIds);
        }

        List<Long> needAddRoleIds = validRoleIds.stream().filter(e -> !existingRoleIds.contains(e)).toList();
        if (!CollectionUtils.isEmpty(needAddRoleIds)) {
            userRoleMapper.insertByUserIdAndRoleIds(userId, needAddRoleIds);
        }
    }

    /**
     * 获取有效的角色 ID 列表。
     *
     * @param roleIds 角色 ID 列表，可以包含无效的角色 ID
     * @return 有效的角色 ID 列表
     */
    private List<Long> getValidRoleIds(List<Long> roleIds) {
        if (Objects.isNull(roleIds)) {
            return Collections.emptyList();
        }
        List<RoleEntity> roles = roleMapper.listByRoleIds(roleIds);
        return roles.stream().map(RoleEntity::getRoleId).toList();
    }

    /**
     * 获取用户。
     *
     * @param userId 用户 ID
     * @return 用户
     */
    public UserEntity getByUserId(Long userId) {
        UserEntity user = userMapper.findByUserId(userId);
        if (Objects.isNull(user)) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXIST);
        }
        return user;
    }
}

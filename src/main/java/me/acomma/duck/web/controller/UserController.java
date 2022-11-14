package me.acomma.duck.web.controller;

import lombok.extern.slf4j.Slf4j;
import me.acomma.duck.model.command.user.AddUserCommand;
import me.acomma.duck.model.entity.UserEntity;
import me.acomma.duck.service.UserService;
import me.acomma.duck.web.form.AddUserForm;
import me.acomma.duck.web.form.AssignRoleToUserForm;
import me.acomma.duck.web.view.UserView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public Long addUser(@Validated @RequestBody AddUserForm form) {
        AddUserCommand command = new AddUserCommand();
        BeanUtils.copyProperties(form, command);

        return userService.addUser(command);
    }

    @PostMapping("/role/assign")
    public void assignRoleToUser(@Validated @RequestBody AssignRoleToUserForm form) {
        userService.assignRoleToUser(form.getUserId(), form.getRoleIds());
    }

    @GetMapping("/detail")
    public UserView getUserDetail(@RequestParam("userId") Long userId) {
        UserEntity entity = userService.getByUserId(userId);

        UserView view = new UserView();
        BeanUtils.copyProperties(entity, view);

        return view;
    }
}

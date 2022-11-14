package me.acomma.duck.web.controller;

import lombok.extern.slf4j.Slf4j;
import me.acomma.duck.model.command.role.AddRoleCommand;
import me.acomma.duck.model.entity.RoleEntity;
import me.acomma.duck.service.RoleService;
import me.acomma.duck.web.form.AddRoleForm;
import me.acomma.duck.web.form.AssignMenuToRoleForm;
import me.acomma.duck.web.view.RoleView;
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
@RequestMapping("/role")
@Validated
@Slf4j
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/add")
    public Long addRole(@Validated @RequestBody AddRoleForm form) {
        AddRoleCommand command = new AddRoleCommand();
        BeanUtils.copyProperties(form, command);

        return roleService.addRole(command);
    }

    @PostMapping("/menu/assign")
    public void assignMenuToRole(@Validated @RequestBody AssignMenuToRoleForm form) {
        roleService.assignMenuToRole(form.getRoleId(), form.getMenuIds());
    }

    @GetMapping("/detail")
    public RoleView getRoleDetail(@RequestParam("roleId") Long roleId) {
        RoleEntity entity = roleService.getByRoleId(roleId);

        RoleView view = new RoleView();
        BeanUtils.copyProperties(entity, view);

        return view;
    }
}

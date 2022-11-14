package me.acomma.duck.web.controller;

import lombok.extern.slf4j.Slf4j;
import me.acomma.duck.model.command.menu.AddMenuCommand;
import me.acomma.duck.model.entity.MenuEntity;
import me.acomma.duck.service.MenuService;
import me.acomma.duck.web.form.AddMenuForm;
import me.acomma.duck.web.view.MenuView;
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
@RequestMapping("/menu")
@Validated
@Slf4j
public class MenuController {
    @Autowired
    private MenuService menuService;

    @PostMapping("/add")
    public Long addMenu(@Validated @RequestBody AddMenuForm form) {
        AddMenuCommand command = new AddMenuCommand();
        BeanUtils.copyProperties(form, command);

        return menuService.addMenu(command);
    }

    @GetMapping("/detail")
    public MenuView getMenuDetail(@RequestParam("menuId") Long menuId) {
        MenuEntity entity = menuService.getByMenuId(menuId);

        MenuView view = new MenuView();
        BeanUtils.copyProperties(entity, view);

        return view;
    }
}

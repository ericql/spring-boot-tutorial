package com.eric.mycat.controller;

import com.eric.mycat.entity.Permission;
import com.eric.mycat.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-5 17:02
 */
@RestController
public class PermissionController {

    @Autowired
    private PermissionService userService;

    @GetMapping("/select")
    public List<Permission> select() {
        return userService.getPermissionList();
    }

    @GetMapping("/insert")
    public Boolean insert(Permission permission) {
        return userService.save(permission);
    }
}

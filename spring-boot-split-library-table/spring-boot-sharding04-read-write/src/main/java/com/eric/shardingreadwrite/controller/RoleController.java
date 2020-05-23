package com.eric.shardingreadwrite.controller;

import com.eric.shardingreadwrite.entity.Role;
import com.eric.shardingreadwrite.service.RoleService;
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
public class RoleController {

    @Autowired
    private RoleService userService;

    @GetMapping("/select")
    public List<Role> select() {
        return userService.getUserList();
    }

    @GetMapping("/insert")
    public Boolean insert(Role role) {
        return userService.save(role);
    }

}

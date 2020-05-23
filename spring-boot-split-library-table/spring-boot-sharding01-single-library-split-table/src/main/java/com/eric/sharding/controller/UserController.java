package com.eric.sharding.controller;

import com.eric.sharding.entity.User;
import com.eric.sharding.service.UserService;
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
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/select")
    public List<User> select() {
        return userService.getUserList();
    }

    @GetMapping("/insert")
    public Boolean insert(User user) {
        return userService.save(user);
    }

}

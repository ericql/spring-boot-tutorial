package com.eric.sharding.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eric.sharding.entity.User;

import java.util.List;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-5 16:59
 */
public interface UserService extends IService<User> {
    /**
     * 保存用户信息
     * @param entity
     * @return
     */
    @Override
    boolean save(User entity);

    /**
     * 查询全部用户信息
     * @return
     */
    List<User> getUserList();
}

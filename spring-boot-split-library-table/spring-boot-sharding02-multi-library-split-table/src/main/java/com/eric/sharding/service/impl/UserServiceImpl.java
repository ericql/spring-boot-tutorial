package com.eric.sharding.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eric.sharding.entity.User;
import com.eric.sharding.mapper.UserMapper;
import com.eric.sharding.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-5 17:00
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public boolean save(User entity) {
        return super.save(entity);
    }

    @Override
    public List<User> getUserList() {
        return baseMapper.selectList(Wrappers.<User>lambdaQuery());
    }
}

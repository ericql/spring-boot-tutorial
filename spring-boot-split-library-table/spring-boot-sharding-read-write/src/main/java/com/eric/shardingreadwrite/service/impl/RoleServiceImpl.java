package com.eric.shardingreadwrite.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eric.shardingreadwrite.entity.Role;
import com.eric.shardingreadwrite.mapper.RoleMapper;
import com.eric.shardingreadwrite.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-5 17:00
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Override
    public boolean save(Role entity) {
        return super.save(entity);
    }

    @Override
    public List<Role> getUserList() {
        return baseMapper.selectList(Wrappers.<Role>lambdaQuery());
    }
}

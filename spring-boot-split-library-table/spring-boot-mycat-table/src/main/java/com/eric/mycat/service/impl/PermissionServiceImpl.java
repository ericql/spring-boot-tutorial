package com.eric.mycat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eric.mycat.entity.Permission;
import com.eric.mycat.mapper.PermissionMapper;
import com.eric.mycat.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-5 17:00
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Override
    public boolean save(Permission entity) {
        return super.save(entity);
    }

    @Override
    public List<Permission> getPermissionList() {
        return baseMapper.selectList(Wrappers.<Permission>lambdaQuery());
    }
}

package com.eric.shardingreadwrite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eric.shardingreadwrite.entity.Role;

import java.util.List;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-5 16:59
 */
public interface RoleService extends IService<Role> {
    /**
     * 保存用户信息
     * @param entity
     * @return
     */
    @Override
    boolean save(Role entity);

    /**
     * 查询全部用户信息
     * @return
     */
    List<Role> getUserList();
}

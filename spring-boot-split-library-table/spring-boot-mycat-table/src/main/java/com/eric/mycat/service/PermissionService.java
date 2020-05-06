package com.eric.mycat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eric.mycat.entity.Permission;

import java.util.List;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-5 16:59
 */
public interface PermissionService extends IService<Permission> {
    /**
     * 保存用户信息
     * @param entity
     * @return
     */
    @Override
    boolean save(Permission entity);

    /**
     * 查询全部用户信息
     * @return
     */
    List<Permission> getPermissionList();
}

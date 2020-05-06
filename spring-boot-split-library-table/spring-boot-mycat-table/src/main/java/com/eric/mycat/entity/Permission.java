package com.eric.mycat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-5 16:54
 */
@Data
@TableName("permission")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Permission extends Model<Permission> {
    /**
     * 主键Id
     */
    private int id;

    /**
     * 权限编码
     */
    private String code;

    /**
     * 权限资源路径
     */
    private String path;
}

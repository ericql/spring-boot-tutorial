package com.eric.sharding.entity;

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
@TableName("user")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class User extends Model<User> {
    /**
     * 主键Id
     */
    private int id;

    /**
     * 名称
     */
    private String name;

    /**
     * 年龄
     */
    private int age;
}

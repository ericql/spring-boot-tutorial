package com.eric.devtools.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-25 19:51
 */
@Data
public class Demo {
    /**
     * 主键
     */
    private long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;
}

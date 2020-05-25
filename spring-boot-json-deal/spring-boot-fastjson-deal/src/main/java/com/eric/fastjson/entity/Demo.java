package com.eric.fastjson.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
    @JSONField(format="yyyy-MM-dd HH:mm")
    private Date createTime;

    /**
     * 备注信息
     * 不返回此字段
     */
    @JSONField(serialize=false)
    private String remarks;
}

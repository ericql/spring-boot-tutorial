package com.eric.helloworld.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Eric
 * @create 2018 04 12 22:12
 */
@Data
public class Demo {
    /**
     * 主键
     */
    private long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}

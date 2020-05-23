package com.eric.sharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-23 11:22
 */
@MapperScan("com.eric.sharding.mapper")
@SpringBootApplication
public class SignleLibrarySplitTableApplication {
    public static void main(String[] args) {
        SpringApplication.run(SignleLibrarySplitTableApplication.class, args);
    }
}

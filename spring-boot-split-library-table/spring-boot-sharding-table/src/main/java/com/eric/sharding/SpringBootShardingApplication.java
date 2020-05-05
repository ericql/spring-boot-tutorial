package com.eric.sharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-5 16:52
 */
@MapperScan("com.eric.sharding.mapper")
@SpringBootApplication
public class SpringBootShardingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootShardingApplication.class, args);
    }
}

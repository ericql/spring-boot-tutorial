package com.eric.shardingreadwrite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-5 18:24
 */
@MapperScan("com.eric.shardingreadwrite.mapper")
@SpringBootApplication
public class SpringBootShardingReadWriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootShardingReadWriteApplication.class, args);
    }

}

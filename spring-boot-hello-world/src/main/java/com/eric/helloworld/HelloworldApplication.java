package com.eric.helloworld;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class HelloworldApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloworldApplication.class, args);
    }

    /**
     * 在/health接口调用的时候，返回多一个属性："mySpringBootApplication":{"status":"UP","hello":"world"}
     */
    public Health health() {
        return Health.up().withDetail("hello", "world").build();
    }
}

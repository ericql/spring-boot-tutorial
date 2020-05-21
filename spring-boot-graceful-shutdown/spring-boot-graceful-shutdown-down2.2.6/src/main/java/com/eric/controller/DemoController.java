package com.eric.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-21 17:20
 */
@RestController
public class DemoController {

    @GetMapping("/demo")
    public String work() throws InterruptedException {
        // 模拟复杂业务耗时处理流程
        Thread.sleep(10 * 1000L);
        return "success";
    }
}

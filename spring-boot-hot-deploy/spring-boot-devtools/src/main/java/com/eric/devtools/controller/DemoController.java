package com.eric.devtools.controller;

import com.eric.devtools.entity.Demo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-25 19:59
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    /**
     *返回demo数据:
     *请求地址：http://127.0.0.1:8080/demo/getDemo
     *@return
     */
    @RequestMapping("/getDemo")
    public Demo getDemo(){
        Demo demo = new Demo();
        demo.setId(1);
        demo.setName("Angel");
        //demo.setCreateTime(new Date());
        return demo;
    }
}

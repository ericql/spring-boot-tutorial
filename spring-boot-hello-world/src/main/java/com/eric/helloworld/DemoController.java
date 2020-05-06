package com.eric.helloworld;

import com.eric.helloworld.entity.Demo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Eric
 * @create 2018 04 12 22:12
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    @GetMapping("getDemo")
    public Demo getDemo() {
        Demo demo = new Demo();
        demo.setId(1);
        demo.setName("Eric");
        demo.setCreateTime(new Date());
        return demo;
    }
}

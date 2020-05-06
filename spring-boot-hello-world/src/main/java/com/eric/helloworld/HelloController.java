package com.eric.helloworld;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Eric
 * @create 2018 04 12 21:39
 */
public class HelloController {
    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }
}

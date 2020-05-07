package com.eric.helloworld;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eric
 * @create 2018 04 12 21:39
 */
@RestController
public class HelloController {
    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }
}

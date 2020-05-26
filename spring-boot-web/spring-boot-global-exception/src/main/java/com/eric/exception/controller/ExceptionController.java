package com.eric.exception.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-26 19:07
 */
@RestController
public class ExceptionController {
    /**
     * 访问：http://127.0.0.1:8080/zeroException这个方法肯定是抛出异常的,那么在控制台就可以看到我们全局捕捉的异常信息了
     * @return
     */
    @RequestMapping("/zeroException")
    public int zeroException(){
        return 100/0;
    }
}

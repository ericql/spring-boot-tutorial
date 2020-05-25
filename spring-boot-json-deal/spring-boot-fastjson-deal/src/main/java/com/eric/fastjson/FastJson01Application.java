package com.eric.fastjson;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-25 20:17
 */
@SpringBootApplication
public class FastJson01Application implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        /*
         * 1、需要先定义一个 convert 转换消息的对象;
         * 2、添加fastJson 的配置信息，比如：是否要格式化返回的json数据;
         * 3、在convert中添加配置信息.
         * 4、将convert添加到converters当中.
         */
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
    }

    public static void main(String[] args) {
        /*
         * 在main方法进行启动我们的应用程序.
         */
        SpringApplication.run(FastJson01Application.class, args);
    }
}

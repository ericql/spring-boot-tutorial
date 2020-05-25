# SpringBoot之JSON处理
## SpringBoot之返回默认JackJSON
### 实体类Demo
```java
@Data
public class Demo {
    /**
     * 主键
     */
    private long id;

    /**
     * 姓名
     */
    private String name;
}
```
### DemoController
```java
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
        return demo;
    }
}
```
### Applicaiton类
```java
@SpringBootApplication
public class JackSonApplication {
    public static void main(String[] args) {
        SpringApplication.run(JackSonApplication.class, args);
    }
}
```
### 测试
&emsp;&emsp;浏览器访问地址：http://127.0.0.1:8080/demo/getDemo返回如下数据：{id: 1,name: "Angel"}
> Spring Boot也是引用了JSON解析包Jackson，那么自然我们就可以在Demo对象上使用Jackson提供的json属性的注解，对时间进行格式化，对一些字段进行忽略等等

## SpringBoot之FastJSON解析数据
### pom依赖
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.0.RELEASE</version>
        <relativePath/>
    </parent>

    <groupId>com.eric</groupId>
    <artifactId>spring-boot-fastjson-deal</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <name>spring-boot-fastjson-deal</name>
    <description>Spring Boot Fastjson 处理</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.68</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```
### 配置FastJson方式一：FastJson01Application
&emsp;&emsp;需要在FastJson01Application中实现WebMvcConfigurer重写方法：configureMessageConverters 添加我们自己定义的json解析框架
```java
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
```
### 配置FastJson方式二：FastJson02Application
```java
@SpringBootApplication
public class FastJson02Application {
    /**
     * 在这里我们使用 @Bean注入 fastJsonHttpMessageConvert
     * @return
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 1、需要先定义一个 convert 转换消息的对象;
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        //2、添加fastJson 的配置信息，比如：是否要格式化返回的json数据;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);

        //3、在convert中添加配置信息.
        fastConverter.setFastJsonConfig(fastJsonConfig);

        HttpMessageConverter<?> converter = fastConverter;
        return new HttpMessageConverters(converter);
    }

    public static void main(String[] args) {
        /*
         * 在main方法进行启动我们的应用程序.
         */
        SpringApplication.run(FastJson01Application.class, args);
    }
}
```
### fastjson解析格式
```java
/**
 * 创建时间
 */
@JSONField(format="yyyy-MM-dd HH:mm")
private Date createTime;

/**
 * 备注信息
 * 不返回此字段
 */
@JSONField(serialize=false)
private String remarks;
```
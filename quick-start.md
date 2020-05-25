# SpringBoot快速开始
## SpringBoot特性
### 特性
1. 创建独立的Spring应用程序
2. 嵌入的Tomcat，无需部署WAR文件
3. 简化Maven配置
4. 自动配置Spring
5. 提供生产就绪型功能，如指标，健康检查和外部配置
6. 开箱即用，没有代码生成，也无需XML配置

### 特性理解
- 为基于Spring的开发提供更快的入门体验
- 开箱即用，没有代码生成，也无需XML配置。同时也可以修改默认值来满足特定的需求。
- 提供了一些大型项目中常见的非功能特性，如嵌入式服务器、安全、指标，健康检测、外部配置等。
- Spring Boot并不是对Spring功能上的增强，而是提供了一种快速使用Spring的方式

## SpringBoot之常用注解
### @SpringBootApplication
&emsp;&emsp;申明让spring boot自动给程序进行必要的配置，这个配置等同于
@Configuration ，@EnableAutoConfiguration 和 @ComponentScan 三个配置
```java
@SpringBootApplication
public class App {
    public static void main(String[] args) {
       SpringApplication.run(ApiCoreApp.class, args);
    }
}
```
### @ResponseBody
&emsp;&emsp;该注解修饰的函数，会将结果直接填充到HTTP的响应体中，一般用于构建RESTful的api，该注解一般会配合@RequestMapping一起使用
```java
@RequestMapping("/test")
@ResponseBody
public String test(){
   return"ok";
}
```

### @Controller
&emsp;&emsp;用于定义控制器类，在spring 项目中由控制器负责将用户发来的URL请求转发到对应的服务接口（service层），一般这个注解在类中，通常方法需要配合注解@RequestMapping
```java
@Controller
@RequestMapping("/demoInfo")
public class DemoController {
    @Autowired
    private DemoInfoService demoInfoService;
   
    @RequestMapping("/hello")
    public String hello(Map<String,Object> map){
       System.out.println("DemoController.hello()");
       map.put("hello","from TemplateController.helloHtml");
       //会使用hello.html或者hello.ftl模板进行渲染显示.
       return "/hello";
    }
}
```
### @RestController
@ResponseBody和@Controller的合集
```java
@RestController
@RequestMapping("/demoInfo2")
public class DemoController2 {
   
    @RequestMapping("/test")
    public String test(){
       return"ok";
    }
}
```
### @RequestMapping
&emsp;&emsp;提供路由信息，负责URL到Controller中的具体函数的映射
### @EnableAutoConfiguration
&emsp;&emsp;Spring Boot自动配置（auto-configuration）：尝试根据你添加的jar依赖自动配置你的Spring应用。例如，如果你的classpath下存在HSQLDB，并且你没有手动配置任何数据库连接beans，那么我们将自动配置一个内存型（in-memory）数据库”。你可以将@EnableAutoConfiguration或者@SpringBootApplication注解添加到一个@Configuration类上来选择自动配置。如果发现应用了你不想要的特定自动配置类，你可以使用@EnableAutoConfiguration注解的排除属性来禁用它们
### @ComponentScan
&emsp;&emsp;表示将该类自动发现（扫描）并注册为Bean，可以自动收集所有的Spring组件，包括@Configuration类。我们经常使用@ComponentScan注解搜索beans，并结合@Autowired注解导入。如果没有配置的话，Spring Boot会扫描启动类所在包下以及子包下的使用了@Service,@Repository等注解的类
### @Configuration
&emsp;&emsp;相当于传统的xml配置文件，如果有些第三方库需要用到xml文件，建议仍然通过@Configuration类作为项目的配置主类——可以使用@ImportResource注解加载xml配置文件
### @Import
&emsp;&emsp;用来导入其他配置类
### @ImportResource
&emsp;&emsp;用来加载xml配置文件
### @Autowired
&emsp;&emsp;自动导入依赖的bean
### @Service
&emsp;&emsp;一般用于修饰service层的组件
### @Repository
&emsp;&emsp;使用@Repository注解可以确保DAO或者repositories提供异常转译，这个注解修饰的DAO或者repositories类会被ComponetScan发现并配置，同时也不需要为它们提供XML配置项
### @Bean
&emsp;&emsp;用@Bean标注方法等价于XML中配置的bean
### @Value
注入Spring boot application.properties配置的属性的值。
```java
@Value(value = "#{message}") 
private String message;
```
### @Qualifier
@Qualifier限定描述符除了能根据名字进行注入，但能进行更细粒度的控制如何选择候选者，具体使用方式如下：
```java
@Qualifier(value = "demoInfoService") 
private DemoInfoService demoInfoService;
```
### @Inject
&emsp;&emsp;等价于默认的@Autowired，只是没有required属性
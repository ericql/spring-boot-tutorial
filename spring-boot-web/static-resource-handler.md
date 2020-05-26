# SpringBoot之静态资源处理
&emsp;&emsp;spring Boot 默认为我们提供了静态资源处理，使用WebMvcAutoConfiguration 中的配置各种属性  
&emsp;&emsp;建议大家使用SpringBoot的默认配置方式，如果需要特殊处理的再通过配置进行修改  
&emsp;&emsp;如果想要自己完全控制WebMVC，就需要在@Configuration注解的配置类上增加@EnableWebMvc，增加该注解以后WebMvcAutoConfiguration中配置就不会生效，你需要自己来配置需要的每一项。这种情况下的配置还是要多看一下WebMvcAutoConfiguration类
## 默认资源映射
&emsp;&emsp;其中默认配置的 /** 映射到 /static (或/public、/resources、/META-INF/resources)  
&emsp;&emsp;当我们访问地址 http://localhost:8080/test.jpg 的时候，显示哪张图片？这里可以直接告诉大家，优先级顺序为：META/resources > resources > static >public
> 其中默认配置的 /webjars/** 映射到 classpath:/META-INF/resources/webjars/  
> 上面的 static、public、resources 等目录都在 classpath: 下面(如 src/main/resources/static)

## 自定义资源映射
&emsp;&emsp;上面我们介绍了Spring Boot 的默认资源映射，一般够用了，那我们如何自定义目录？   
&emsp;&emsp;这些资源都是打包在jar包中的，然后实际应用中，我们还有很多资源是在管理系统中动态维护的，并不可能在程序包中，对于这种随意指定目录的资源，如何访问？
### 自定义目录
以增加/myres/* 映射到 classpath:/myres/* 为例的代码处理为： 实现类继承 WebMvcConfigurerAdapter 并重写方法 addResourceHandlers
```java
@Configuration
public class MyWebAppConfigurer  extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/myres/**").addResourceLocations("classpath:/myres/");
        super.addResourceHandlers(registry);
    }
}
```
&emsp;&emsp;访问myres 文件夹中的test.jpg 图片的地址为 http://localhost:8080/myres/test.jpg  
&emsp;&emsp;这样使用代码的方式自定义目录映射，并不影响Spring Boot的默认映射，可以同时使用  
&emsp;&emsp;如果我们将/myres/* 修改为 /* 与默认的相同时，则会覆盖系统的配置，可以多次使用 addResourceLocations 添加目录，优先级先添加的高于后添加的
> 其中addResourceLocations 的参数是动参，可以这样写addResourceLocations(“classpath:/img1/”, “classpath:/img2/”,“classpath:/img3/”)

### 使用外部目录
&emsp;&emsp;如果我们要指定一个绝对路径的文件夹（如 D:/data/api_files ），则只需要使用 addResourceLocations 指定即可。  
&emsp;&emsp;可以直接使用addResourceLocations 指定磁盘绝对路径，同样可以配置多个位置，注意路径写法需要加上file:
```java
registry.addResourceHandler("/api_files/**").addResourceLocations("file:D:/data/api_files")
```
### 配置文件配置
&emsp;&emsp;上面是使用代码来定义静态资源的映射，其实Spring Boot也为我们提供了可以直接在application.properties（或.yml）中配置的方法(默认值为 /**)
```properties
spring.mvc.static-path-pattern=# 默认值为 classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/
spring.resources.static-locations=这里设置要指向的路径，多个使用英文逗号隔开
```
- 使用<font color='red'>spring.mvc.static-path-pattern</font> 可以重新定义pattern，如修改为 /myres/** ，则访问static 等目录下的fengjing.jpg文件应该为http://localhost:8080/myres/fengjing.jpg ，修改之前为http://localhost:8080/fengjing.jpg
- 使用<font color='red'>spring.resources.static-locations</font>可以重新定义 pattern 所指向的路径，支持 classpath: 和 file:

> 注意: spring.mvc.static-path-pattern 只可以定义一个，目前不支持多个逗号分割的方式

### 页面中使用
&emsp;&emsp;上面几个例子中也已经说明了怎么访问静态资源，其实在页面中使用不管是jsp还是freemarker，并没有什么特殊之处，也我们平时开发web项目一样即可  
下面是我的index.jsp：
```jsp
<body>
    <imgalt="读取默认配置中的图片"src="${pageContext.request.contextPath}/pic.jpg">

    <br/>

    <imgalt="读取自定义配置myres中的图片"src="${pageContext.request.contextPath}/myres/fengjing.jpg">
</body>
```
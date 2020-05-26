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
## 解决js/css缓存问题
&emsp;&emsp;问题的提出：我们对于我们编写的js和css文件，经常会做一些改变，由于浏览器缓存，用户本地的资源还是旧资源，一般为了解决这种情况导致的问题，我们会可能会选择在资源文件后面加上参数“版本号”或其他方式  
```jsp
<scripttype="text/javascript"src="/js/common.js?v=1.0.1"></script>
```
&emsp;&emsp;使用这种方式，当我们文件修改后，手动修改版本号来达到URL文件不被浏览器缓存的目的。同样也存在很多文件都需要修改的问题，或者有的人会增加时间戳的形式，这样是最不可取的，每次浏览器都需要为服务器增加了不必要的压力。Spring在解决这种问题方面，提供了2中解决方式
### 回顾默认资源映射
- 默认配置的/** 映射到 /static(或/public、/resources、/META-INF/resources)
- 其中默认配置的/webjars/** 映射到 classpath:/META-INF/resources/webjars/

### 使用webjars
&emsp;&emsp;我们在Web开发中，前端页面中用了越来越多的JS或CSS，如jQuery等等，平时我们是将这些Web资源拷贝到Java的目录下，这种通过人工方式拷贝可能会产生版本误差，拷贝版本错误，前端页面就无法正确展示  
&emsp;&emsp;WebJars 就是为了解决这种问题衍生的，将这些Web前端资源打包成Java的Jar包，然后借助Maven这些依赖库的管理，保证这些Web资源版本唯一性  
&emsp;&emsp;WebJars 就是将js, css 等资源文件放到 classpath:/META-INF/resources/webjars/ 中，然后打包成jar 发布到maven仓库中  

以jQuery为例,文件存放结构为：
```js
META-INF/resources/webjars/jquery/2.1.4/jquery.js
META-INF/resources/webjars/jquery/2.1.4/jquery.min.js
META-INF/resources/webjars/jquery/2.1.4/jquery.min.map
META-INF/resources/webjars/jquery/2.1.4/webjars-requirejs.js
```
使用方式就是在pom.xml文件中添加配置
```xml
<dependency>
   <groupId>org.webjars</groupId>
   <artifactId>jquery</artifactId>
   <version>2.1.4</version>
</dependency>
```
&emsp;&emsp;Spring Boot 默认将 /webjars/** 映射到 classpath:/META-INF/resources/webjars/ ，结合我们上面讲到的访问资源的规则，便可以得知我们在页面中引入jquery.js的方法为：<script type="text/javascript" src="/webjars/jquery/2.1.4/jquery.js"></script>版本号统一管理  
&emsp;&emsp;我们实际开发中，可能会遇到升级版本号的情况，如果我们有100多个页面，几乎每个页面上都有按上面引入jquery.js 那么我们要把版本号更换为3.0.0，一个一个替换显然不是最好的办法。 使用webjars的webjars-locator就可以解决以上的问题，那么具体要怎么操作呢  

#### 首先在pom.xml添加webjars-locator的依赖
```xml
<dependency>
   <groupId>org.webjars</groupId>
   <artifactId>webjars-locator</artifactId>
</dependency>
```
#### 增加一个WebJarsController
&emsp;&emsp;这个Controller会将以webjarslocator路径拦截，然后重新组装处理，具体代码如下
```java
package com.kfit;

@Controller
public class WebJarsController {
    privatefinal WebJarAssetLocator assetLocator = new WebJarAssetLocator();
    @ResponseBody
    @RequestMapping("/webjarslocator/{webjar}/**")
    public ResponseEntity<Object> locateWebjarAsset(@PathVariable String webjar, HttpServletRequest request) {
        try {
            String mvcPrefix = "/webjarslocator/" + webjar + "/"; // This prefix must match the mapping path!
            String mvcPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            String fullPath = assetLocator.getFullPath(webjar, mvcPath.substring(mvcPrefix.length()));
            return new ResponseEntity<>(new ClassPathResource(fullPath), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
```
最后在页面中使用的方式：
```js
<script type="text/javascript" src="/webjarslocator/jquery/jquery.js"></script>
```
### Spring静态资源版本映射之资源名称md5方式
&emsp;&emsp;Spring 默认提供了静态资源版本映射的支持,当我们的资源内容发生改变时，由于浏览器缓存，用户本地的资源还是旧资源，为了防止这种情况发生导致的问题。我们可能会选择在资源文件后面加上参数”版本号”或其他方式<scripttype="text/javascript"src="/js/demo.js?v=1.0.1"></script>  
&emsp;&emsp;使用这种方式，当我们文件修改后，手工修改版本号来达到URL文件不被浏览器缓存的目的。同样也存在很多文件都需要修改的问题。或者有的人会增加时间戳的方式，这样我认为是最不可取的，每次浏览器都要请求为服务器增加了不必要的压力  

然而Spring在解决这种问题方面，提供了2种解决方式。 第一种方式就是MD5的方式  
1. 修改 application.properties 配置文件
```properties
spring.resources.chain.strategy.content.enabled=true
spring.resources.chain.strategy.content.paths=/**
```
2. 创建 ResourceUrlProviderController 文件
```java
package com.kfit;

@ControllerAdvice
public class ResourceUrlProviderController {
    @Autowired
    private ResourceUrlProvider resourceUrlProvider;
    @ModelAttribute("urls")
    public ResourceUrlProvider urls() {
        return this.resourceUrlProvider;
    }
}
```
3. 在页面中进行使用
```js
<scripttype="text/javascript"src="${urls.getForLookupPath('/js/demo.js') }"></script>
```
如果使用的thymeleaf模板引擎的话，那么需要这么进行编写：
```js
<scripttype="text/javascript"th:src="${urls.getForLookupPath('/js/demo.js') }"></script>
```
当我们访问页面后，HTML中实际生成的代码为：
```html
<scripttype="text/javascript"src="/js/demo--ef8d9e1da763788be348c78ea32a3c6d.js"></script>
```

### Spring静态资源版本映射之资源版本号方式
&emsp;&emsp;资源版本号方式对所有资源的统一版本控制,不像上面一个md5是针对文件的除了在 application.properties中的配置有所区别，页面使用和md5的一样
```properties
spring.resources.chain.strategy.fixed.enabled=true
spring.resources.chain.strategy.fixed.paths=/js/**,/v1.0.0/**
spring.resources.chain.strategy.fixed.version=v1.0.0
```
这样配置后，以上面 common.js 为例，实际页面中生成的HTML代码为：
```html
<scripttype="text/javascript"src="/v1.0.0/js/demo.js"></script>
```
### md5版本号方式的处理方式
&emsp;&emsp;页面中首先会调用urls.getForLookupPath方法，返回一个/v1.0.0/js/demo.js或/css/demo-c6b7da8fffc9be141b48c073e39c7340.js然后浏览器发起请求  
&emsp;&emsp;当请求的地址为md5方式时，会尝试url中的文件名中是否包含-，如果包含会去掉后面这部分，然后去映射的目录（如/static/）查找/js/common.js文件，如果能找到就返回  
&emsp;&emsp;当请求的地址为版本号方式时，会在url中判断是否存在/v1.0.0 ，如果存在，则先从URL中把 /v1.0.0 去掉，然后再去映射目录查找对应文件，找到就返回
### 总结
1. 我们使用第三方的库时，建议使用webjars的方式，通过动态版本号(webjars-locator 的方式)来使用(因为第三方库在项目开发中变动频率很小，即便是变动也是版本号的修改)  
2. 我们使用自己存放在静态资源映射目录中的资源的时候，建议使用md5 资源文件名的方式来使用(项目开发中一些css、js文件会经常修改)  
3. 项目素材文件建议放到 classpath:/static (或其他)目录中，打包在项目中，通过CMS维护的一些图片和资源，我们使用配置引用到具体的磁盘绝对路径来使用  
4. 注意使用md5文件名方式的时候，Spring 是有缓存机制的，也就是说，在服务不重启的情况下，你去变动修改这些资源文件，其文件名的md5值并不会改变，只有重启服务再次访问才会生效。如果需要每次都获取实际文件的md5值，需要重写相关类来实现，我们不建议这样做，因为一直去计算文件md5值是需要性能代价的
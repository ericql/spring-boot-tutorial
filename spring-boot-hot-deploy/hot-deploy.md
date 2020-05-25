# SpringBoot之热部署
## devtools使用
### 原理介绍
&emsp;&emsp;spring-boot-devtools 是一个为开发者服务的一个模块，其中最重要的功能就是自动应用代码更改到最新的App上面去。原理是在发现代码有更改之后，重新启动应用，但是速度比手动停止后再启动还要更快，更快指的不是节省出来的手工操作的时间  
&emsp;&emsp;其深层原理是使用了两个ClassLoader，一个Classloader加载那些不会改变的类（第三方Jar包），另一个ClassLoader加载会更改的类，称为  restart ClassLoader  
&emsp;&emsp;这样在有代码更改的时候，原来的restart ClassLoader 被丢弃，重新创建一个restart ClassLoader，由于需要加载的类相比较少，所以实现了较快的重启时间(5秒以内)
### 使用方法
#### pom依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
    <scope>true</scope>
</dependency>
```
#### 添加spring-boot-maven-plugin
```xml
<build>
	<plugins>
		<plugin>
	       <groupId>org.springframework.boot</groupId>
	       <artifactId>spring-boot-maven-plugin</artifactId>
	       <configuration>
	          	<!--fork :  如果没有该项配置，肯呢个devtools不会起作用，即应用不会restart -->
	            <fork>true</fork>
	         </configuration>
	     </plugin>
	</plugins>
</build>
```
### 说明
1. devtools会监听classpath下的文件变动，并且会立即重启应用（发生在保存时机），注意：因为其采用的虚拟机机制，该项重启是很快的。
2. devtools可以实现页面热部署（即页面修改后会立即生效，这个可以直接在application.properties文件中配置spring.thymeleaf.cache=false来实现(这里注意不同的模板配置不一样)

### 测试方法
1. 修改类-->保存：应用会重启
2. 修改配置文件-->保存：应用会重启
3. 修改页面-->保存：应用会重启，页面会刷新（原理是将spring.thymeleaf.cache设为false）

### 补充
&emsp;&emsp;默认情况下，/META-INF/maven，/META-INF/resources，/resources，/static，/templates，/public这些文件夹下的文件修改不会使应用重启，但是会重新加载(devtools内嵌了一个LiveReload server，当资源发生改变时，浏览器刷新)  
&emsp;&emsp;如果想改变默认的设置，可以自己设置不重启的目录：spring.devtools.restart.exclude=static/**,public/**，这样的话，就只有这两个目录下的文件修改不会导致restart操作了  
&emsp;&emsp;如果要在保留默认设置的基础上还要添加其他的排除目录：spring.devtools.restart.additional-exclude  
&emsp;&emsp;如果想要使得当非classpath下的文件发生变化时应用得以重启，使用：spring.devtools.restart.additional-paths，这样devtools就会将该目录列入了监听范围

### 关闭自动重启
&emsp;&emsp;设置 spring.devtools.restart.enabled 属性为false，可以关闭该特性。可以在application.properties中设置，也可以通过设置环境变量的方式
```java
public static void main(String[] args){
    System.setProperty("spring.devtools.restart.enabled","false");
    SpringApplication.run(MyApp.class, args);
}
```

### devtools问题总结
#### 不能重启原因分析
1. 对应的spring-boot版本是否正确，这里使用的是1.4.1版本以上
2. 是否加入plugin以及属性<fork>true</fork>
3. Eclipse Project 是否开启了Build Automatically（我自己就在这里栽了坑，不知道为什么我的工具什么时候关闭了自动编译的功能）
4. 如果设置SpringApplication.setRegisterShutdownHook(false)，则自动重启将不起作用

#### devtools restart session设置为restart之后依然存在
&emsp;&emsp;在devtools中在编写一个普通的程序时，都是能够运行的很正常的，但是和其它进行一起运行的时候，可能就会出现各种各样的问题了，比如：登录之后，然后修改了一些代码之后devtools就restart,然后重新一访问又重新登录了  
&emsp;&emsp;如果restart之后session失效的话，那么就会造成需要重新登录问题，这个问题很好解决，只需要在application.properties添加如下配置：
```properties
server.session.persistent=true
```
> 注意在spring boot 1.3.3 / 1.4.0默认就是true

#### 修改模板文件restart不生效
&emsp;&emsp;模板文件不生效主要是由于模板有缓存，只要把缓存给关闭即可，配置application.properties文件
```properties
#模板引擎：thymeleaf;
spring.thymeleaf.cache=false
#Javascript 语言的模板引擎：Mustache
spring.mustache.cache=false
#模板引擎：velocity
spring.velocity.cache=false
#groovy模板
spring.groovy.template.cache
```
#### causing ClassCastException while getting from cache
&emsp;&emsp;当在使用缓存的时候，可能就会抛出如上的异常信息了，这是devtools已知的限制。当缓存条目被反序列化时，对象无法附加到合适的类加载器  

有一些方法可以修复这样的问题：
1. 当运行在开发环境的时候，禁用缓存，配置spring.cache.type=NONE
2. 使用一个不同的缓存管理器（假如你正在使用spring boot 1.3,你能够强制使用simple cache manager通过在application-dev.properties配置spring.cache.type，并且使用在你的IDE中启用dev profile）
3. 在应用程序类加载器配置缓存，这种比较复杂，需要新建META-INF/spring-devtools.properties文件进行配置，支持restart.exclude. and restart.include

例如：
```properties
restart.include.companycommonlibs=/mycorp-common-[\\w-]+\.jar
restart.include.projectcommon=/mycorp-myproj-[\\w-]+\.jar
```
# SpringBoot之服务器
## SpringBoot之服务配置和部署
&emsp;&emsp;Spring Boot 其默认是集成web容器的，启动方式由像普通Java程序一样，main函数入口启动。其内置Tomcat容器或Jetty容器，具体由配置来决定（默认Tomcat）。当然你也可以将项目打包成war包，放到独立的web容器中（Tomcat、weblogic等等），当然在此之前你要对程序入口做简单调整
### 内嵌Server配置
```properties
#项目contextPath，一般在正式发布版本中，我们不配置
server.context-path=/springboot
# 错误页：指定发生错误时，跳转的URL。请查看BasicErrorController。
server.error.path=/error
# 服务端口，默认为8080
server.port=8080
# session最大超时时间(分钟)，默认为30
server.session-timeout=60
# 该服务绑定IP地址，启动服务器时如本机不是该IP地址则抛出异常启动失败，只有特殊需求的情况下才配置
# server.address=192.168.16.11
```
### Tomcat配置项
```properties
# tomcat最大线程数，默认为200
server.tomcat.max-threads=800
# tomcat的URI编码
server.tomcat.uri-encoding=UTF-8
# 存放Tomcat的日志、Dump等文件的临时文件夹，默认为系统的tmp文件夹（如：C:\Users\Angel\AppData\Local\Temp）
server.tomcat.basedir=D:/springboot-tomcat-tmp
# 打开Tomcat的Access日志，并可以设置日志格式的方法：
#server.tomcat.access-log-enabled=true
#server.tomcat.access-log-pattern=
# accesslog目录，默认在basedir/logs
#server.tomcat.accesslog.directory=
# 日志文件目录
logging.path=H:/springboot-tomcat-tmp
# 日志文件名称，默认为spring.log
logging.file=myapp.log
```
### Jetty选择
&emsp;&emsp;我们刚刚说了spring boot默认是Tomcat，如果你要选择Jetty,也非常简单，只需要把pom.xml中的tomcat依赖排除，并加入Jetty容器的依赖接口，如下配置：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <!-- 排除默认的tomcat,引入jetty容器. -->
    <exclusions>
      <exclusion>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
      </exclusion>
    </exclusions>
</dependency>

<!-- jetty 容器. -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```
### 部署
#### 打成jar包直接部署
#### 打成war包部署到容器
```xml
<packaging>war</packaging>
```
使用命令mvn clean package打包后,同一般J2EE项目一样部署到web容器
#### 修改启动类,继承SpringBootServletInitializer并重写configure方法
```java
@SpringBootApplication
publicclass App extends SpringBootServletInitializer{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        returnbuilder.sources(this.getClass());
    }
   
    publicstaticvoid main(String[] args) {
       SpringApplication.run(App.class, args);
    }
}
```
## SpringBoot之Undertow高性能web服务器
### Undertow介绍
&emsp;&emsp;Undertow 是一个采用 Java 开发的灵活的高性能 Web 服务器，提供包括阻塞和基于 NIO 的非堵塞机制。Undertow 是红帽公司的开源产品，是 Wildfly 默认的 Web 服务器  
&emsp;&emsp;Undertow 提供一个基础的架构用来构建 Web 服务器，这是一个完全为嵌入式设计的项目，提供易用的构建器 API，完全兼容 Java EE Servlet 3.1 和低级非堵塞的处理器
### Undertow特点
1. **轻量化** - Undertow 是一个Web 服务器，但它不像传统的Web 服务器有容器的概念，它由两个核心jar包组成，使用API加载一个Web应用可以使用小于10MB的内存
2. **HTTP Upgrade 支持** - 设计WildFly时一个重要的考虑因素是在云环境中减少端口数量的需求。在云环境中，一个系统可能运行了几百个甚至几千个WildFly实例。基于HTTP使用HTTP Upgrade可以升级成多种协议，Undertow提供了复用这些协议的能力
3. **Web Socket 支持** - 对Web Socket的完全支持，用以满足Web应用现在面对巨大数量的客户端，以及对JSR-356规范的支持
4. **Servlet 3.1 的支持** - Undertow支持Servlet 3.1，提供了一个机会来构建一个超越Servlet规范、对开发人员非常友好的系统
5. **可嵌套性** - Web 服务器不在需要容器，我们只需要通过API在J2SE代码下快速搭建Web服务

### Undertow社区
- [Undertow社区主页](http://undertow.io/)：包括Undertow相关的所有新闻，消息
- [Undertow源代码](https://github.com/undertow-io/)：包括所有Undertow相关的代码
- [嵌入式Web服务器Undertow](http://www.oschina.net/p/undertow)
- [JBoss 系列九十三： 高性能非阻塞 Web 服务器 Undertow](http://blog.csdn.net/kylinsoong/article/details/19432375)

### 示例代码
#### pom依赖
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
	<exclusions>
		<exclusion>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-tomcat</artifactId>
		</exclusion>
	</exclusions>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-undertow</artifactId>
</dependency>
```
重新启动运用程序，查看控制台的输出信息，能够看到如下信息：
```
main] .UndertowEmbeddedServletContainerFactory :
main] io.undertow.websockets.jsr     
main] io.undertow.servlet
```
### Undertow之配置信息
Spring boot也为我们提供了Undertow常用的配置信息(application.properties):
```properties
server.undertow.accesslog.dir= # Undertow access log directory.
server.undertow.accesslog.enabled=false # Enable access log.
server.undertow.accesslog.pattern=common # Format pattern for access logs.
server.undertow.accesslog.prefix=access_log. # Log file name prefix.
server.undertow.accesslog.rotate=true # Enable access log rotation.
server.undertow.accesslog.suffix=log # Log file name suffix.
server.undertow.buffer-size= # Size of each buffer in bytes.
server.undertow.buffers-per-region= # Number of buffer per region.
server.undertow.direct-buffers= # Allocate buffers outside the Java heap.
server.undertow.io-threads= # Number of I/O threads to create for the worker.
server.undertow.max-http-post-size=0 # Maximum size in bytes of the HTTP post content.
server.undertow.worker-threads= # Number of worker threads
```
### 与Tomcat内存分析比较
#### Tomcat内存情况
&emsp;&emsp;我们知道，默认情况下,Spring Boot使用Tomcat来作为内嵌的Servlet容器。我们启动项目之后可以使用VisualVM进行查看应用所占的内存情况:
1. 进入到你的jdk的安装目录下bin，找到：jvisualvm.exe，双击打开
2. 双击打开之后,你能在左边操作栏找到【本地】--【应用程序的包名】--【双击打开】

![tomcat监控--首页.png](https://i.loli.net/2020/05/26/Z9z2AXpdKx5Oomw.png)

![tomcat监控--CPU页.png](https://i.loli.net/2020/05/26/PBJUqSGgrl5HnRM.png)

![tomcat监控--线程页.png](https://i.loli.net/2020/05/26/hv6TyozbN2ZjK5t.png)

![tomcat监控--堆内存页.png](https://i.loli.net/2020/05/26/wmUrPJWTelyiaSc.png)

&emsp;&emsp;以上是我使用Spring Boot默认的方式启动应用后，用VisualVM监控到的内存占用情况：堆内存占用50M，17个线程被开启。没有比较就没有伤害，一比我吓一跳，哈哈，单独的一个tomcat是无法看出来效果的，我们看看Undertow的情况下
#### Undertow内存使用情况
启动项目，用VisualVM监控到的信息显示：堆内存占用50M,16个线程被开启
![undertow监控--线程页.png](https://i.loli.net/2020/05/26/YWsE2k3zvxqD4Zd.png)

![undertow监控--堆内存页.png](https://i.loli.net/2020/05/26/xZbaTv9GkzotnA1.png)

这里只是一个Hello World代码，实际项目中应该会更明显
[toc]
# SpirngBoot2.3实现优雅停机
## 优雅停机
&emsp;&emsp;就是对应用进程发送停止指令之后，能保证**正在执行的业务操作不受影响，可以继续完成已有请求的处理，但是停止接受新请求**  
&emsp;&emsp;在 Spring Boot 2.3 中增加了新特性**优雅停止**，目前 Spring Boot 内置的四个嵌入式 Web 服务器（Jetty、Reactor Netty、Tomcat 和 Undertow）以及反应式和基于 Servlet 的 Web 应用程序都支持优雅停止。
## 项目实例
### pom依赖
&emsp;&emsp;首先创建一个 Spring Boot 的 Web 项目，版本选择 2.3.0.RELEASE，Spring Boot 2.3.0.RELEASE 版本内置的 Tomcat 为 9.0.35
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
    </parent>

    <groupId>com.eric</groupId>
    <artifactId>spring-boot-graceful-shutdown-up2.3</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <name>spring-boot-graceful-shutdown-up2.3</name>
    <description>SpringBoot2.3 实现优雅停机</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
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
&emsp;&emsp;Spring容器的关闭主要依靠上述的actuator内置的接口实现Spring容器的管理.  
&emsp;&emsp;其中通过 Actuator 关闭 Spring 容器的入口代码在 org.springframework.boot.actuate.context 包下 ShutdownEndpoint
 类中，主要的就是执行 doClose() 方法关闭并销毁 applicationContext，有兴趣的可以自己研究下  
下方我们通过配置来实现Web容器的关闭
### yml配置
&emsp;&emsp;需要在 application.yml 中添加一些配置来启用优雅停止的功能
```yaml
# 开启优雅停止web容器，默认为 IMMEDIATE--立即停止
server:
  shutdown: graceful

# 最大等待时间
spring:
  lifecycle:
    timeout-per-shutdown-phase: 30s

# 暴露 shutdown 接口
management:
  endpoint:
    shutdown:
      enabled: true
  endpoints:
    web:
      exposure:
        include: shutdown
```
&emsp;&emsp;上述配置实现web容器的平滑关闭，平滑关闭内置的 Web 容器（以 Tomcat 为例）的入口代码在 org.springframework.boot.web.embedded.tomcat
 的 GracefulShutdown 里，大概逻辑就是先停止外部的所有新请求，然后再处理关闭前收到的请求，有兴趣的可以自己研究下  
### Controller
&emsp;&emsp;在 controller 包下创建一个 WorkController 类，并有一个 work 方法，用来模拟复杂业务耗时处理流程，具体代码如下：
```java
@RestController
public class WorkController {
    @GetMapping("/work")
    public String work() throws InterruptedException {
        // 模拟复杂业务耗时处理流程
        Thread.sleep(10 * 1000L);
        return "success";
    }
}
```
## 测试
### 请求work
启动项目，先用 Postman 请求 http://localhost:8080/work 处理业务, 一直请求中.....

### 调用优雅停机
在这个时候，调用 http://localhost:8080/actuator/shutdown 就可以执行优雅地停止，返回结果如下：
```
{
    "message": "Shutting down, bye..."
}
```
### 停机后测试
在这个时候，发起新的请求 http://localhost:8080/work，会没有反应，出现 "Could not get any response",再回头看第一个请求，返回了结果：success
### 查看服务日志
```
2020-05-21 17:37:01.283  INFO 18044 --- [     Thread-255] o.s.b.w.e.tomcat.GracefulShutdown        : Commencing graceful shutdown. Waiting for active requests to complete
2020-05-21 17:37:07.770  INFO 18044 --- [tomcat-shutdown] o.s.b.w.e.tomcat.GracefulShutdown        : Graceful shutdown complete
2020-05-21 17:37:08.236  INFO 18044 --- [     Thread-255] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
```
从日志中也可以看出来，当调用 shutdown 接口的时候，会先等待请求处理完毕后再优雅地停止

# SpirngBoot旧版本实现优雅停机
GitHub 上 [issue](https://github.com/spring-projects/spring-boot/issues/4657) 里 Spring Boot 开发者提供的一种方案:  
## 依赖
依赖基本与上保持一致, 只是选取的 Spring Boot 版本为 2.2.6.RELEASE
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.6.RELEASE</version>
    </parent>

    <groupId>com.eric</groupId>
    <artifactId>spring-boot-graceful-shutdown-down2.2.6</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <name>spring-boot-graceful-shutdown-up2.2.6</name>
    <description>SpringBoot2.2.6 实现优雅停机</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
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
## yml配置
```yaml
# 暴露 shutdown 接口
management:
  endpoint:
    shutdown:
      enabled: true
  endpoints:
    web:
      exposure:
        include: shutdown
```
## 实现关闭
&emsp;&emsp;实现 ApplicationListener<ContextClosedEvent> 接口，因为要监听 Spring 容器的关闭事件，即当前的 ApplicationContext 执行 close
() 方法，这样我们就可以在请求处理完毕后进行 Tomcat 线程池的关闭，具体的实现代码如下：
```java
@Bean
public GracefulShutdown gracefulShutdown() {
    return new GracefulShutdown();
}

private static class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {
    private static final Logger log = LoggerFactory.getLogger(GracefulShutdown.class);

    private volatile Connector connector;

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        this.connector.pause();
        Executor executor = this.connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                threadPoolExecutor.shutdown();
                if (!threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                    log.warn("Tomcat thread pool did not shut down gracefully within 30 seconds. Proceeding with forceful shutdown");
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
```
此时还需要在启动过程中添加到内嵌的 Tomcat 容器中，然后等待监听到关闭指令时执行，addConnectorCustomizers 方法可以把定制的 Connector 行为添加到内嵌的 Tomcat 中，具体代码如下：
```java
@Bean
public ConfigurableServletWebServerFactory tomcatCustomizer() {
    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
    factory.addConnectorCustomizers(gracefulShutdown());
    return factory;
}
```
到此为止，内置的 Tomcat 容器平滑关闭的操作就完成了, 另外使用 Actuator 的同时要注意安全问题：
- 可以通过引入 security 依赖，打开安全限制并进行身份验证
- 设置单独的 Actuator 管理端口并配置只对内网开放
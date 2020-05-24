# SpringBoot之HelloWorld
## 新建maven工程或SpringBoot工程
工程名是spring-boot-hello-world
## pom中添加SprinBoot的maven依赖
### 引入spring-boot-parent
&emsp;&emsp;它可以提供dependency management,也就是说依赖管理，引入以后在申明其他dependency的时候就不需要version了
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.6.RELEASE</version>
    <relativePath/>
</parent>
```
### 引入spring-boot-starter-web
&emsp;&emsp;因为开发的是web工程,所有需要在pom.xml中引入spring-boot-web,官方解释spring-boot-start-web包含了spring webmvc和tomcat等web开发的特性
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
### plugin
&emsp;&emsp;如果我们要直接Main启动spring，那么以下plugin必须要添加，否则是无法启动，如果使用maven的spring-boot:run启动可以不需要此配置
```xml
<build>
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
	</plugins>
</build>
```
## 编写启动类
```java
@RestController
@SpringBootApplication
public class App {
  
  @RequestMapping("/")
  public String hello(){
    return"Hello world!";
  }
  
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
```
- @SpringBootApplication声明让springboot自动给程序进行必要的配置，等价于以默认属性使用@Configuration,@EnableAutoConfiguration和@ComponentScan
- @RestController返回json字符串的数据，直接可以编写Resful的接口

## 运行程序
- 方式一:右键Run As -> Java Application
- 方式二:project– Run as – Maven build –在Goals里输入spring-boot:run ,然后Apply,最后点击Run

## 错误分析
### JDK版本不匹配
| name | Servlet version | Java version |
| :---: | :---: | :---: |
| Tomcat 8 | 3.1 | Java 7+ |
| Tomcat 7 | 3.0 | Java 6+ |
| Jetty 9 | 3.1 | Java 7+ |
| Jetty 8 | 3.0 | Java 6+ |
| Undertow 1.1 | 3.1 | Java 7+ |

### 访问404
#### 确定地址是否输入正确,此路径非彼路径
- 确保访问地址正确，比如：http://127.0.0.1:8080/demo
- 这里需要注意端口号，可以在启动的时候，查看到，另外地址URI可以在启动的时候，查看到是否编码成功被注入了

#### 是否用对注解,此注解非彼注解
&emsp;&emsp;刚入门容易搞错注解，常犯错的注解：@Controller,正确的注解是@RestController;或者是@Controller+@ResponseBody，所以@RestController等价于@Controller加上@ResponseBod

#### 包路径是否正确,此包非彼包
&emsp;&emsp;Spring Boot默认是扫描@SpringBootApplication注解的类的同包以及子包下的类。比如：我们有一个包com.kfit ，在此包下有我们的App.java
```java
@SpringBootApplication  
public class App{  
       public static void main(String[] args){  
        SpringApplication.run(App.class,args);  
   }  
}
```
&emsp;&emsp;那你如果是你编写的代码在org.kfit下的，这明显是不同包下，这样访问的也会出现404，当然Spring Boot是否可以支持不同包呢，答案是可以的，但是默认的配置是不可以的，我们需要添加一些配置信息，注解指定扫描的包名称
#### 确认类包是否正确,此类包非彼类包
&emsp;&emsp;查看使用的注解@ResController和@RequestMapping的包路径是否正确

## SpringBoot之更改JDK版本
&emsp;&emsp;spring Boot在编译的时候，是有默认JDK版本的，如果我们期望使用我们要的JDK版本的话，那么要怎么配置呢  
&emsp;&emsp;只需要修改pom.xml文件的<build>-- <plugins>加入一个plugin
```xml
<plugin>
   <artifactId>maven-compiler-plugin</artifactId>
   <configuration>
      <source>1.8</source>
      <target>1.8</target>
   </configuration>
</plugin>
```
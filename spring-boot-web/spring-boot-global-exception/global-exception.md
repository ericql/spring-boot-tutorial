# 全局异常捕获
## 统一异常处理
1. 新建一个Class,这里取名为GlobalDefaultExceptionHandler
2. 在class上添加注解，@ControllerAdvice;
3. 在class中添加一个方法
4. 在方法上添加@ExcetionHandler拦截相应的异常信息；
5. 如果返回的是View -- 方法的返回值是ModelAndView;
6. 如果返回的是String或者是Json数据，那么需要在方法上添加@ResponseBody注解

在一个项目中的异常我们都会统一进行处理的
1. 新建一个类GlobalDefaultExceptionHandler，
2. 在class注解上@ControllerAdvice,
3. 在方法上注解上@ExceptionHandler(value = Exception.class)

具体代码如下:
```java
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public void defaultErrorHandler(HttpServletRequest req, Exception e)  {
        e.printStackTrace();
        System.out.println("GlobalDefaultExceptionHandler.defaultErrorHandler()");
    }
}
```
## 异常捕获
```java
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
```
精确异常捕获：
```java
@Controller
public class ExceptionHandlingController {
 
  // @RequestHandler methods
  ...
  
  // Exception handling methods
  
  // 将预定义异常转换为HTTP状态代码
  @ResponseStatus(value=HttpStatus.CONFLICT, reason="Data integrity violation")  // 409
  @ExceptionHandler(DataIntegrityViolationException.class)
  public void conflict() {
    // Nothing to do
  }
  
  // 指定用于显示错误的特定视图的名称
  @ExceptionHandler({SQLException.class,DataAccessException.class})
  public String databaseError() {
    //返回一个错误页面的逻辑视图名称，以通常的方式传递给视图解析器。值得注意的是，除了_not_可用这个观点（不添加到模型）但见“扩展exceptionhandlerexceptionresolver”下面
    return "databaseError";
  }
 
  // 全部控件-设置一个模型并返回视图名或考虑子类exceptionhandlerexceptionresolver（见下文）
  @ExceptionHandler(Exception.class)
  public ModelAndView handleError(HttpServletRequest req, Exception exception) {
    logger.error("Request: " + req.getRequestURL() + " raised " + exception);
 
    ModelAndView mav = new ModelAndView();
    mav.addObject("exception", exception);
    mav.addObject("url", req.getRequestURL());
    mav.setViewName("error");
    return mav;
  }
}
```
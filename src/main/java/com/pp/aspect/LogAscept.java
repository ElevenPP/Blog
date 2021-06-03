package com.pp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect//切面
@Component//组件扫描
public class LogAscept {

    //拿到日志记录器
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.pp.web.*.*(..))")//@Pointcut定义log（）方法为一个切面，execution(* com.pp.blog.*.*(..))表示com.pp.blog文件夹下所有类的所有方法都拦截
    public void log() {

    }

    //切面之前
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest= attributes.getRequest();
        String url = httpServletRequest.getRequestURL().toString();
        String ip = httpServletRequest.getRemoteAddr();

        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." +joinPoint.getSignature().getName();//类名+方法名
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url,ip,classMethod,args);
        logger.info("Request : {}", requestLog);
    }
    //切面之后
    @After("log()")
    public void doAfter() {
//        logger.info("------doAfter------");
    }
    //拦截返回的内容
    @AfterReturning(returning = "result",pointcut = "log()")
    public void daAfterReturn(Object result) {
        logger.info("Result : {}",result);
    }

    private class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}

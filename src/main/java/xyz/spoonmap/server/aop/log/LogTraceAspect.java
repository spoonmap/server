package xyz.spoonmap.server.aop.log;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class LogTraceAspect {

    @Pointcut("execution(* xyz.spoonmap.server.member.controller.v1.*.*(..))")
    public void controllerV1() {

    }

    @Around("controllerV1()")
    public Object entry(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        LogTraceThreadLocal.set();

        log.info("[{}] {}", LogTraceThreadLocal.getId(), request.getRequestURI());
        Object proceed = pjp.proceed();

        LogTraceThreadLocal.remove();

        return proceed;
    }

    @AfterThrowing(pointcut = "controllerV1()", throwing = "e")
    public void logError(Exception e) {
        log.error("[{}] {}: {}", LogTraceThreadLocal.getId(), e.getStackTrace()[0], e.getMessage());
        LogTraceThreadLocal.remove();
    }

}

package com.qatestlab.olegvolik.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
/** This class is used for logging the key application
 * actions. Every time the method save() in service
 * classes invoked (as well as any method annotated with
 * @Loggable) the information is logged to the console.
 *
 * @author Oleg Volik
 */
public class MyAppLogger {

    @Before("@annotation(com.qatestlab.olegvolik.aop.Loggable)")
    public void beforeSearchAdvice (JoinPoint joinPoint) {
        log.info("Invoking method " + joinPoint.toString());
        log.info("Passing arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "@annotation(com.qatestlab.olegvolik.aop.Loggable)", returning = "returnObject")
    public void afterSearchAdvice (JoinPoint joinPoint, Object returnObject) {
        log.info("Values returned by method " + joinPoint.toString() + " : " + returnObject);
    }

    @Around("execution(* com.qatestlab.olegvolik.service.*.save(*))")
    public Object objectSavingAdvice (ProceedingJoinPoint proceedingJoinPoint) {
        log.info("Saving object " + Arrays.toString(proceedingJoinPoint.getArgs()));
        Object val = null;
        try {
            val = proceedingJoinPoint.proceed();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        log.info("Saved object: " + val);
        return val;
    }
}

package com.sap.concur.coreint.configurations.spring.aop;

import com.sap.concur.coreint.configurations.spring.annotations.LazyComponent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Mohan Sharma
 */
@Aspect
@LazyComponent
@Slf4j
public class ElapsedTimeAspect {

    @Around("@annotation(com.sap.concur.coreint.configurations.spring.annotations.ElapsedTime)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        var startTime = System.currentTimeMillis();
        var obj = proceedingJoinPoint.proceed();
        var duration = System.currentTimeMillis() - startTime;
        log.info("Elapsed time of {} class's {} method is {}", proceedingJoinPoint
                .getSignature()
                .getDeclaringTypeName(),
            proceedingJoinPoint
                .getSignature()
                .getName(), duration + " ms.");
        return obj;
    }
}

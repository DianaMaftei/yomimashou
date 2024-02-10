package com.yomimashou.creator.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Before("execution(* com.yomimashou.creator..*.*(..) )")
    public void logBefore(JoinPoint joinPoint) {
        log.debug("executing " + joinPoint.getSignature().toShortString() + " with args " + Arrays.toString(joinPoint.getArgs()));
    }
}

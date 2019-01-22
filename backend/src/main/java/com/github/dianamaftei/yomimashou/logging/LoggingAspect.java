package com.github.dianamaftei.yomimashou.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.github.dianamaftei.yomimashou..*.*(..) )")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("executing " + joinPoint.getSignature().toShortString() + " with args " + Arrays.toString(joinPoint.getArgs()));
    }

}
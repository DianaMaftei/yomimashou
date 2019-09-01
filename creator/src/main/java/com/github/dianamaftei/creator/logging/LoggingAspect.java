package com.github.dianamaftei.creator.logging;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.github.dianamaftei.creator..*.*(..) )")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("executing " + joinPoint.getSignature().toShortString() + " with args " + Arrays.toString(joinPoint.getArgs()));
    }

}
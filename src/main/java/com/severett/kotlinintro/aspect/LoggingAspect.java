package com.severett.kotlinintro.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Aspect
@Service
@Slf4j
public class LoggingAspect {
    private static final String LOG_MESSAGE_FORMAT_WITH_PARAM = "- Execution time of {}.{} with params: {} was: {} ms";
    private static final String LOG_PERFORMANCE_OFFERED_SERVICE_CONTROLLER
            = "PERFORMANCE_OFFERED_SERVICE_CONTROLLER " + LOG_MESSAGE_FORMAT_WITH_PARAM;
    private static final String LOG_PERFORMANCE_PETS_CONTROLLER
            = "PERFORMANCE_PETS_CONTROLLER " + LOG_MESSAGE_FORMAT_WITH_PARAM;

    @Around("execution(* com.severett.kotlinintro.controller.OfferedServiceController.*(..)))")
    public Object logOfferedServiceControllerExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, LOG_PERFORMANCE_OFFERED_SERVICE_CONTROLLER);
    }

    @Around("execution(* com.severett.kotlinintro.controller.PetsController.*(..)))")
    public Object logPetsControllerExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, LOG_PERFORMANCE_PETS_CONTROLLER);
    }

    private Object logExecutionTime(ProceedingJoinPoint joinPoint, String logIndicator) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();
        stopWatch.stop();
        log.info(
                logIndicator,
                joinPoint.getTarget().getClass().getName(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs(),
                stopWatch.getTotalTimeMillis()
        );
        return result;
    }
}

package com.severett.kotlinintro.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.StopWatch

private const val LOG_MESSAGE_FORMAT_WITH_PARAM = "- Execution time of {}.{} with params: {} was: {} ms"
private const val LOG_PERFORMANCE_OFFERED_SERVICE_CONTROLLER =
    "PERFORMANCE_OFFERED_SERVICE_CONTROLLER $LOG_MESSAGE_FORMAT_WITH_PARAM"
private const val LOG_PERFORMANCE_PETS_CONTROLLER =
    "PERFORMANCE_PETS_CONTROLLER $LOG_MESSAGE_FORMAT_WITH_PARAM"

@Aspect
@Service
class LoggingAspect {
    private val log = LoggerFactory.getLogger(LoggingAspect::class.java)

    @Around("execution(* com.severett.kotlinintro.controller.OfferedServiceController.*(..)))")
    @Throws(
        Throwable::class
    )
    fun logOfferedServiceControllerExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        return logExecutionTime(joinPoint, LOG_PERFORMANCE_OFFERED_SERVICE_CONTROLLER)
    }

    @Around("execution(* com.severett.kotlinintro.controller.PetsController.*(..)))")
    @Throws(Throwable::class)
    fun logPetsControllerExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        return logExecutionTime(joinPoint, LOG_PERFORMANCE_PETS_CONTROLLER)
    }

    private fun logExecutionTime(joinPoint: ProceedingJoinPoint, logIndicator: String): Any? {
        val stopWatch = StopWatch()
        stopWatch.start()

        val result = joinPoint.proceed()
        stopWatch.stop()
        log.info(
            logIndicator,
            joinPoint.target.javaClass.name,
            joinPoint.signature.name,
            joinPoint.args,
            stopWatch.totalTimeMillis
        )
        return result
    }
}

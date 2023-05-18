package dev.yudin.aspects;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Log4j
@Aspect
@Component
public class LoggingAspect {

	@Before("dev.yudin.aspects.AopExpression.forAppFlow()")
	public void before(JoinPoint joinPoint) {
		String method = joinPoint.getSignature().toShortString();
		log.info("====>> Calling method: " + method);

		Object[] args = joinPoint.getArgs();
		for (var arg : args) {
			log.info("====> Income argument: " + arg);
		}
	}

	@AfterReturning(
			pointcut = "dev.yudin.aspects.AopExpression.forAppFlow()",
			returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		String method = joinPoint.getSignature().toShortString();
		log.info("====>> Calling method: " + method);
		log.info("<<==== Return of method: " + result);
	}
}

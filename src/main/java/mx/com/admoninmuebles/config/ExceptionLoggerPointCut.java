package mx.com.admoninmuebles.config;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class ExceptionLoggerPointCut {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@AfterThrowing(pointcut="execution(* mx.com.admoninmuebles.controller.*.*(..))", throwing="exception")
	public void logControllerException(JoinPoint joinPoint, Exception exception) {
		logger.error( ExceptionUtils.getStackTrace( exception ) );
	}
	
	@AfterThrowing(pointcut="execution(* mx.com.admoninmuebles.service.*.*(..))", throwing="exception")
	public void logServiceException(JoinPoint joinPoint, Exception exception) {
		logger.error( ExceptionUtils.getStackTrace( exception ) );
	}


}

package com.weatherApp.common.Aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Order(1)
@Slf4j
public class LoggingAspect {

	@Pointcut("execution(* com.weatherApp.*..*Controller.*(..))")
	public void controllerMethod() {}
	
    @Pointcut("execution(* com.weatherApp..*.execute(..))")
    public void useCaseMethods() {}
    
    @Pointcut("execution(* com.weatherApp..*Repo.*(..))")
    public void repositoryMethods() {}




    
	@Around("controllerMethod()")
	public Object logAroundController(ProceedingJoinPoint joinPoint ) throws Throwable {
		
		  String className = joinPoint.getTarget().getClass().getSimpleName();
	        String methodName = joinPoint.getSignature().getName();
	        Object[] args =    joinPoint.getArgs();	        
	        log.info("[ CONTROLLER ] - Entering {}.{}() with args : {} ",
	        		className,
	        		methodName,
	        		args
	        		);
		
	        try {
				
	        	Object result = joinPoint.proceed();
	        	log.info("[ CONTROLLER ] - Exiting {}.{}() with results : {} ",
	        			className,
	        			methodName,
	        			result
	        			);
	        	return result;
	        	
			} catch (Exception e) {
				log.error("[ EXCEPTION ] - Exception in {}.{}() : {} - {} ",
						className,
						methodName,
						e.getClass().getSimpleName(),
						e.getMessage()
						);
				throw e;
			}
	}
	
	@Around("useCaseMethods()")
	public Object logAroundUseCase(ProceedingJoinPoint joinPoint) throws Throwable {
		
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getSimpleName();
		Object [] args  = joinPoint.getArgs(); 
	
		long startTime = System.currentTimeMillis();
		log.info("[ USECASE ] - Entering usecase method : {}.{}() ",
				className,
				methodName
				);
		
		try {
			
			
			Object result = joinPoint.proceed();
			
			long executionTime = System.currentTimeMillis() - startTime;
			log.info("[ USECASE ] - Exiting useCase Method : {}.{}(), executed method in {} ms",
					className,
					methodName,
					executionTime
					);
			
			return result;
		} catch (Exception e) {
			
			log.error("[ EXCEPTION ] - Exception occurred: {} - {} ",
					e.getClass().getSimpleName(),
					e.getMessage()
					);
			
			throw e;
		}
		
	}
	
	@Around("repositoryMethods()")
	public Object logAroundRepository(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getSimpleName();
		Object [] args  = joinPoint.getArgs(); 
	
		
		log.info("[ REPOSITORY ] - Repository Call : {}.{}()",
				className,
				methodName
				
				);
		
		Object result = joinPoint.proceed();
		
		log.info("[ REPOSITORY ] - Repository result : {}",
				result
				);
		return result;
	}
	
	
	 
	
}
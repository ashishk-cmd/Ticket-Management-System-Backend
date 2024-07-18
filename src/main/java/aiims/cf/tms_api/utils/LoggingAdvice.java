package aiims.cf.tms_api.utils;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoggingAdvice {

//    private static final Logger log = LogManager.getLogger(LoggingAdvice.class);
	Logger log = LoggerFactory.getLogger(LoggingAdvice.class);

	 // Advice to log exceptions

     @Pointcut("execution(* aiims.cf.tms_api.controllers.*.*(..))")
     public void allMethodsInTmsPackage() {}

     @AfterThrowing(pointcut = "allMethodsInTmsPackage()", throwing = "exception")
     public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
       String methodName = joinPoint.getSignature().getName();
       Object[] args = joinPoint.getArgs();
       log.error("User:{}, got Exception from:{}, with Input parameter values:{}, and getting exception as:{}",getLoggedInUsername(), methodName, Arrays.toString(args), exception.getMessage());
    }

    //  @Around("allMethodsInTmsPackage()")
    @Around(value = "allMethodsInTmsPackage() && @annotation(org.springframework.web.bind.annotation.PostMapping)")    
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().toString();
        Object[] array = pjp.getArgs();
        
        log.info("User : " + getLoggedInUsername() + " is calling from IP : " + getClientIpAddress() + 
        		" method invoked " + className + " : " + methodName + "()" + " arguments : " + mapper.writeValueAsString(array));
        Object object = pjp.proceed();
        
        log.info("User : " + getLoggedInUsername() + " calling from IP : " + getClientIpAddress() + 
        		" has been completed " + className + " : " + methodName + "()" + "Response : " + mapper.writeValueAsString(object));
        return object;
    }
    
    private String getClientIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getRemoteAddr();
    }
    
    private String getLoggedInUsername() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (authentication != null && authentication.isAuthenticated()) { 
    		return authentication.getName();
    	}else {
    		return "Anonymous";
    	}
    }
}

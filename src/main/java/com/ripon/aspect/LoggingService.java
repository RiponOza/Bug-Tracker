package com.ripon.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingService {
	
	@Pointcut("execution(public boolean com.ripon.service.UserService.validateUser(..))")
	public void userLogin() {
		
	}
	
	//@Before("userLogin()")
	@AfterReturning(value = "userLogin()", returning = "val")
	public void loginLogging(Boolean val) {
		if(val==true) {
			System.out.println("Login success.....");
		}else {
			System.out.println("Login failure.....");
		}
	}
	
	@AfterThrowing("userLogin()")
	public void errorLogin() {
		System.out.println("Error occured........");
	}
	

}

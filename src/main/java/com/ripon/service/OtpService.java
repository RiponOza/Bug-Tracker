package com.ripon.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class OtpService {
	
	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;
	
	
	public static String generateOTP() 
    {  
        int randomPin   =(int) (Math.random()*9000)+1000;
        String otp  = String.valueOf(randomPin);
        return otp;
    }
	
	
	public String sendOtp(String email) {
		String otp = generateOTP();
		String sub = "OTP from bug tracker.";
		String body = "Your OTP is " + otp + ". Do not share it with any one. Thank you !";
		String to = email;
		emailService.sendSimpleMessage(to, sub, body);
		return otp;
	} 
	
	@Async
	public void invalidateOtp(HttpSession session) {
		try {
			Thread.sleep(1000*60*5);
			session.invalidate();
			return;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
	}

}

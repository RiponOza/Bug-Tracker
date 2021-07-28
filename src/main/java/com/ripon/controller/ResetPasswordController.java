package com.ripon.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ripon.entity.User;
import com.ripon.service.EmailService;
import com.ripon.service.OtpService;
import com.ripon.service.UserService;

@Controller
public class ResetPasswordController {
	
	@Autowired
	UserService userService;
	@Autowired
	OtpService otpService;
	
	
	@GetMapping("/reset-password")
	public String renderResetPassword() {
		return "ResetPassword";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, Model model, HttpSession session) {
		//Email no is not registered
		if(!userService.isEmailAvailable(email)) {
			model.addAttribute("status_failure", "This email id is not regitered !");
			return "ResetPassword";
		}
		// otp successfully sent
		User user = userService.getUserByEmail(email);
		String otp = otpService.sendOtp(email);
		session.setAttribute("otp", otp);
		session.setAttribute("phone", user.getPhone());
		//otpService.invalidateOtp();
		model.addAttribute("status_success", "OTP is sent to your email id. Please check your inbox");
		return "ChangePassword";
	}
	
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam("otp")String otp, @RequestParam("password")String password, @RequestParam("confirmPassword") String confirmPassword, Model model, HttpSession session) {
		password = password.trim();
		confirmPassword = confirmPassword.trim();
		otp = otp.trim();
		if(otp.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
			model.addAttribute("status_failure", "You must fill up all the fields !");
			return "ChangePassword";
		}
		
		if(!password.equals(confirmPassword)) {
			model.addAttribute("status_failure", "Password and confirm password must be same !");
			return "ChangePassword";
		}
		
		if(!otp.equals(session.getAttribute("otp").toString())) {
			model.addAttribute("status_failure", "You entered wrong OTP !");
			return "ChangePassword";
		}
		User user = userService.getUser(session.getAttribute("phone").toString());
		boolean status = userService.updatePassword(user.getEmail(), password);
		
		if(status) {
			model.addAttribute("status_success", "Your password is successfully updated !");
			session.invalidate();
			return "login";
		} else {
			model.addAttribute("status_failure", "Oops, something went wrong. Please try again !");
			session.invalidate();
			return "login";
		}
		
	}
	

}

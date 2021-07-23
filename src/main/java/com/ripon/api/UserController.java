package com.ripon.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ripon.entity.User;
import com.ripon.service.UserService;

@CrossOrigin
@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/noauth/is-email-available")
	public boolean isEmailAvailable(@RequestParam("email") String email) {
		return userService.isEmailAvailable(email.trim());
	}
	
	// validation before adding user in project
	@GetMapping("/noauth/is-user-valid")
	public int isUserValid(@RequestParam("email") String email) {
		User user = userService.getUser(email);
		if(user==null) {
			return -1; // user not valid
		}
		if(!(user.getRole().equals("DV")||user.getRole().equals("TS"))) {
			return -2; // user is not developer or tester
		}
		return 1;
		
	}

}

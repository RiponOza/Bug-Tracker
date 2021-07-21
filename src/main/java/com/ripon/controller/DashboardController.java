package com.ripon.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ripon.entity.User;
import com.ripon.service.UserService;

@Controller
public class DashboardController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private User user;
	
	@GetMapping("/dashboard")
	public String getDashboardPage(HttpSession session, Model model) {
		
		if(session.getAttribute("userid") == null) {
			return "redirect:/login";
		}
		
		user = userService.getUser(session.getAttribute("userid").toString());
		if(user != null) {
			model.addAttribute("name", user.getFname() + " " + user.getLname());
			model.addAttribute("email", user.getEmail());
			model.addAttribute("phone", "" + user.getCountryCode() + user.getPhone());
			model.addAttribute("role", User.ROLE.get(user.getRole()));
			model.addAttribute("image", "/profile-image/"+user.getImage());
			return "dashboard";
		} else {
			return "redirect:/login";
		}
		
	}
	

}









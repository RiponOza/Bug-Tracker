/* 
 * @ Author Ripon Oza
 * This controller is for testing purpose.
 *  */
package com.ripon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TestController {

	// use of path variable.
	// it prints the given id in url on browser.
	@RequestMapping("/book/{id}")
	public String pathVariableTest(@PathVariable("id") int id) {
		System.out.println("Your id is : " + id);
		return "login";
	}


	// creates exception
	@GetMapping("/dostuff")
	public String doStuff() throws ClassNotFoundException {
		// null pointer exception occurs here
		String s = null;
		System.out.println(s.toLowerCase());
		return "login";
	}


	// spring mvc redirect example
	@RequestMapping("/redirect")
	public String redirectHome() {
		return "redirect:/login";
	}

	// check session attribute "userid" value
	@RequestMapping("/session")
	public String showSessionAttribute(HttpSession session) {
		if(session.getAttribute("userid") != null) {
			String userid = session.getAttribute("userid").toString();
			System.out.println(userid);
		} else {
			System.out.println("session is empty.");
		}
		return "redirect:/login";
	}

}







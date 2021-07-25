/**This is login controller. All the routing for user login process is done here.
 * @author Ripon Oza
 * @since 2021
 * @version 1.0
 */
package com.ripon.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.ripon.entity.Login;
import com.ripon.entity.User;
import com.ripon.service.UserService;

@Controller
//@SessionAttributes("userid")
public class LoginController {

	@Autowired
	UserService userService;
	@Autowired
	Login loginEntity;

	
	@GetMapping({ "/", "/login" })
	public String renderlogin(HttpServletResponse resp, Model model, HttpSession session) {
			if(session.getAttribute("userid")!=null) {
				return "redirect:/dashboard";
			}
			model.addAttribute("loginDetail", loginEntity);
			Cookie c1 = new Cookie("Creator", "Ripon_Oza");
			resp.addCookie(c1);
			return "login";
	}

	
	@PostMapping("/login")//@Valid @ModelAttribute("loginDetail") Login loginDetail,
	public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
		
		if (email.isBlank() || password.isBlank()) {
			model.addAttribute("status_failure", "fields shouldn't be empty !");
			return "login";
		}
		
		if (userService.validateUser(email, password)) {
			// set session data
			User user = userService.getUserByEmail(email);
			session.setAttribute("userid", user.getPhone());
			session.setAttribute("role", user.getRole());
			session.setMaxInactiveInterval(20 * 60);
			return "redirect:/dashboard";
		} else {
			model.addAttribute("email", email);
			model.addAttribute("password", password);
			model.addAttribute("status_failure", "Wrong email or password !");
			return "login";
		}
	}

	
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session) {
		String userid = session.getAttribute("userid").toString();
		response.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
	    response.setHeader("Pragma","no-cache");
	    response.setDateHeader("Expires", 0);
		if (userid != null) {
			session.invalidate();
			userid = null;
			model.addAttribute("status_success", "successfully logged out");
		}
		return "login";
	}
	
	
	
	
}

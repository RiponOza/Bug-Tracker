/**This is login controller. All the routing for user login process is done here.
 * @author Ripon Oza
 * @since 2021
 * @version 1.0
 */
package com.ripon.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

	/**
	 * This method renders the login page. If user is already logged in, then it
	 * will show directly dashboard. If the user is not logged in, it will render
	 * the login page. Before rendering login page it creates a model attribute
	 * named 'loginDetail'
	 * 
	 * @param model
	 * @param session
	 * @return login page if session is not available, else returns dashboard for
	 *         the logged in user.
	 */
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

	/**
	 * This handler handles the user login process. It holds user login detail in
	 * 'loginDetail' model object. If any error occurs it returns the login page
	 * with error message. If everything goes well then it saves user id and user
	 * name in two session attributes 'userid' and 'username' for 20min interval and
	 * then returns the dashboard.
	 * 
	 * @param loginDetail It is a model object which holds the login detail of the
	 *                    user in Login entity class.
	 * @param result
	 * @param session
	 * @param model
	 * @return login page with error message if any error occures . Else returns
	 *         dashbord.
	 */
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

	
	/**
	 * This handler is used in logout process. It invalidate the current session if
	 * any session is available. Then redirects the login page
	 * 
	 * @param model
	 * @param session
	 * @return login page when user loges out.
	 */
	@RequestMapping("/logout")
	public String logout(Model model, HttpSession session) {
		String userid = session.getAttribute("userid").toString();
		if (userid != null) {
			session.invalidate();
			userid = null;
			// model.addAttribute("status", "successfully logged out");
		}
		return "redirect:/login";
	}
	
	
	
	
}

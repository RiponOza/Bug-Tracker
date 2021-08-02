package com.ripon.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.cj.Session;
import com.ripon.entity.User;
import com.ripon.service.UserService;

@Controller
public class ProfileController {
	
	@Autowired
	private UserService userService;
	
	@Value("${profile-image-path}")
	private String profileImagePath;

	@GetMapping("/profile")
	public String getProfile(Model model ,HttpSession session) {
		if(session==null) {
			return "redirect:/login";
		}
		
		User user = userService.getUser(session.getAttribute("userid").toString());
		model.addAttribute("name", user.getFname() + " " + user.getLname());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("phone", user.getPhone());
		model.addAttribute("image", "/profile-image/" + user.getImage());
		model.addAttribute("role", User.ROLE.get(user.getRole()));
		return "profile";
	}
	
	@GetMapping("/edit-profile")
	public String getEditProfile(Model model ,HttpSession session) {
		if(session==null) {
			return "redirect:/login";
		}
		User user = userService.getUser(session.getAttribute("userid").toString());
		model.addAttribute("fname", user.getFname());
		model.addAttribute("lname", user.getLname());
		model.addAttribute("phone", user.getPhone());
		model.addAttribute("email", user.getEmail() );
		model.addAttribute("image", "/profile-image/" + user.getImage());
		model.addAttribute("role", User.ROLE.get(user.getRole()));
		return "edit_profile";
	}
	
	@PostMapping("/edit-profile")
	public String editProfile(@RequestParam("fname")String fname, @RequestParam("lname")String lname, Model model, HttpSession session) {
		if(session==null) {
			return "redirect:/login";
		}
		fname = fname.trim();
		lname = lname.trim();
		if(fname.isBlank() || lname.isBlank()) {
			return "redirect:/profile";
		}
		User user = userService.getUser(session.getAttribute("userid").toString());
		//user.setPhone(session.getAttribute("userid").toString());
		user.setFname(fname);
		user.setLname(lname);
		boolean status = userService.updateUser(user);
		
		return "redirect:/profile";
	}
	
	@PostMapping("/upload-profile-pic")
	public String uploadProfilePic(@RequestParam("imageFile") MultipartFile file, HttpSession session) throws IOException {
		if(file.isEmpty()) {
			return "redirect:/profile";
		}
		
		String contentType = file.getContentType();
		if(contentType.equals("image/jpg") || contentType.equals("image/png") || contentType.equals("image/jpeg")) {
			userService.saveProfilePic(session.getAttribute("userid").toString() , file);
		}
		
		return "redirect:/profile";
	}
	
	
	@GetMapping(value = "/profile-image/{image-name}", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getImage(@PathVariable("image-name") String imageName, HttpServletResponse response, HttpSession session) throws IOException {
		if(session.getAttribute("userid")==null) {
			return null;
		}
		response.setContentType("image/jpeg");
		File file = new File(profileImagePath + imageName);
		if(!file.exists()) {
			return null;
		}
		FileInputStream fin = new FileInputStream(file);
		return IOUtils.toByteArray(fin);
		
	}
	
	
	@GetMapping("/remove-profile-pic")
	public String deleteProfilePic( HttpSession session ) {
		userService.deleteProfilePic(session.getAttribute("userid").toString());
		return "redirect:/edit-profile";
	}

	
	
}

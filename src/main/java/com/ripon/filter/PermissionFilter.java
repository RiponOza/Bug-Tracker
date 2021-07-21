package com.ripon.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ripon.entity.User;
import com.ripon.service.UserService;

@Component
public class PermissionFilter extends HttpFilter {
	
	@Autowired
	UserService userService;
	
	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String url = request.getRequestURI();
		if(url.equals("/dashboard")) {
			User user = userService.getUser(request.getSession().getAttribute("userid").toString());
			//System.out.println("Role : " + user.getRole());
		}
		super.doFilter(request, response, chain);
	}

}

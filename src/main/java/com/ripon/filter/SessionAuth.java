package com.ripon.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;


@Component
public class SessionAuth extends HttpFilter {

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException
	{
		
		try {
			String url = request.getRequestURI();
			// file and image caching control
			if(!url.contains("/public/") && !url.contains("/profile-image/") && !url.contains("/show-resource")) {
				response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
			    response.setHeader("Pragma","no-cache");
			    response.setHeader("Expires", "0");
			}
			
			if( url.contains("/noauth/") || url.contains("/public/") || url.equals("/") || url.equals("/login") || url.equals("/register") || url.equals("/reset-password") || url.equals("/send-otp")) {
				//super.doFilter(request, response, chain);
				chain.doFilter(request, response);
			}
			else {
				if(request.getSession().getAttribute("userid")!=null) {
					chain.doFilter(request, response);
				} else {
					response.sendRedirect("/");
				}
			}
		} catch (Exception e) {
			response.sendRedirect("/");
		}
	}
	

}

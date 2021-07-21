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
		//System.out.println(request.getRequestURI());
		try {
			String url = request.getRequestURI();
			if(url.contains("/public/") || url.equals("/") || url.equals("/login") || url.equals("/register") || url.equals("/reset-password") || url.equals("/send-otp")) {
				//super.doFilter(request, response, chain);
				chain.doFilter(request, response);
			}
			else {
				if(request.getSession().getAttribute("userid")!=null) {
					//super.doFilter(request, response, chain);
					chain.doFilter(request, response);
				} else {
//					PrintWriter writer = response.getWriter();
//					writer.print("Not Authorized !");
//					writer.close();
					response.sendRedirect("/");
				}
			}
		} catch (Exception e) {
			response.sendRedirect("/");
		}
	}

}

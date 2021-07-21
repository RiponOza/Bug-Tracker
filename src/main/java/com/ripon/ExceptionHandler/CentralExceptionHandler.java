/* 
 * @Author Ripon Oza
 * Centralized exception handling 
 * */

package com.ripon.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class CentralExceptionHandler {

	// exception handling
	// null pointer exception
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ NullPointerException.class })
	public String NullPointerExceptionHandler(Model m, NullPointerException e) {
		// printing exception detail on browser
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		m.addAttribute("exception", "Null Pointer Exception");
		m.addAttribute("desc", sw.toString());
		return "exception";
	}

	// exception handling
	// number format exception
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ NumberFormatException.class })
	public String numberFormatExceptionHandler(Model m, NumberFormatException e) {
		// printing exception detail on browser
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		m.addAttribute("exception", "Number Format Exception");
		m.addAttribute("desc", sw.toString());
		return "exception";
	}

	// exception handling
	// number format exception
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public String genericExceptionHandler(Model m, Exception e) {
		// printing exception detail on browser
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		m.addAttribute("exception", e.getClass().getName().toString());
		m.addAttribute("desc", sw.toString());
		return "exception";
	}
	

}

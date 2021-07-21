/**
 * This entity class holds the data of user. There are total eight fields in the 
 * class with getters and setters.
 */
package com.ripon.entity;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

/* holds info about the user */
@Component
public class User {
	
	@Email
	@NotBlank(message = "Email can't be empty !")
	private String email;
	@Size(min = 2, max = 12, message = "First name must be between 2 - 12 characters !")
	private String fname;
	@Size(min = 2, max = 12, message = "Last name must be between 2 - 12 characters !!")
	private String lname;
	@Size(min = 2, max = 4, message = "You must fill country code !!")
	private String countryCode;
	@Size(min = 10, max = 12, message = "Phone no must be between 10 - 12 characters !!")
	private String phone;
	@NotBlank(message = "Chose your role !!")
	private String role;
	@Size(min = 4, max = 12, message = "Password name must be between 4 - 12 characters !")
	private String password;
	@NotBlank(message = "Confirm Password can't be empty")
	private String confirmPassword;
	private String image;
	
	public static final Map<String, String> ROLE = new HashMap<String, String>();
	
	public User() {
		ROLE.put("AD", "admin");
		ROLE.put("PM", "project manager");
		ROLE.put("DV", "developer");
		ROLE.put("TS", "tester");
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getPhone() {
		return phone;
	}
	
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", fname=" + fname + ", lname=" + lname + ", countryCode=" + countryCode
				+ ", phone=" + phone + ", role=" + role + ", password=" + password + ", confirmPassword="
				+ confirmPassword + ", image=" + image + "]";
	}

	

}

package com.ripon.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ripon.entity.EnvData;

@RestController
public class TestApi {
	
	@Autowired
	EnvData envData;

	@GetMapping("/hello")
	public String hello() {
		return "Hello";
	}
	
}

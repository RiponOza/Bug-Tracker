package com.ripon.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ripon.entity.EnvData;
import com.ripon.entity.User;
import com.ripon.service.ProjectService;

@RestController
public class TestApi {
	
	@Autowired
	EnvData envData;
	@Autowired
	JdbcTemplate jt;
	@Autowired
	ProjectService ps;

	@GetMapping("/hello")
	public String hello() {
		return "Hello";
	}
	
	@GetMapping("/test")
	public List<User> test() {
		return ps.getUsersOfProject("4");
	}
	
	@GetMapping("/dv")
	public List<Map<String, Object>> getDevelopers(String role) {
		role="DV";
		return jt.queryForList("SELECT * FROM User WHERE role=?;",role);
	}
	@GetMapping("/pm")
	public List<Map<String, Object>> getProjectManagers(String role) {
		role="PM";
		return jt.queryForList("SELECT * FROM User WHERE role=?;",role);
	}
	@GetMapping("/ts")
	public List<Map<String, Object>> getTester(String role) {
		role="TS";
		return jt.queryForList("SELECT * FROM User WHERE role=?;",role);
	}
	
}

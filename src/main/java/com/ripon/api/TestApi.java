package com.ripon.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ripon.entity.EnvData;
import com.ripon.entity.TicketResource;
import com.ripon.entity.User;
import com.ripon.service.ProjectService;
import com.ripon.service.TicketResourceService;

@RestController
public class TestApi {
	
	@Autowired
	EnvData envData;
	@Autowired
	JdbcTemplate jt;
	@Autowired
	ProjectService ps;
	@Autowired
	TicketResourceService trs;

	@GetMapping("/hello")
	public String hello() {
		return "Hello";
	}
	
	@GetMapping("/noauth/test")
	public List<TicketResource> test() {
		return trs.getTicketResources("6");
		//return FilenameUtils.getExtension(".gitignore");
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

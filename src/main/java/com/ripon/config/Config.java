package com.ripon.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import com.ripon.entity.EnvData;


@SpringBootConfiguration
//@PropertySource("application.properties")
public class Config {
	
	@Value("${database.url}")
	private String url;
	@Value("${database.user}")
	private String user;
	@Value("${database.password}")
	private String password;

	
	@Bean
	@Scope("singleton")
	public BasicDataSource getDataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setUrl(url);
		ds.setUsername(user);
		ds.setPassword(password);
		ds.setInitialSize(30);
		//System.out.println("Data Source Bean is created");
		return ds;
	}
	
	@Bean
	@Scope("singleton")
	public JdbcTemplate getJdbcTemplate() {
		JdbcTemplate jt = new JdbcTemplate();
		jt.setDataSource(getDataSource());
		return jt;
	}

	@Bean
	@Scope("singleton")
	public EnvData getEnvData() {
		EnvData env = new EnvData();
		if(System.getProperty("os.name").equals("Windows 10")) {
			env.setImage_path("C:/images/");
			env.setHost("localhost");
		}
		return env;
	}
}

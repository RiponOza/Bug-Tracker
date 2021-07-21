package com.ripon.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ripon.entity.Project;

public class ProjectMapper implements RowMapper<Project> {

	@Override
	public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
		Project project = new Project();
		project.setId(rs.getLong("id")+"");
		project.setTitle(rs.getString("title"));
		project.setDesc(rs.getString("descr"));
		project.setManagerId(rs.getLong("manager_id")+"");
		project.setAdminId(rs.getLong("admin_id")+"");
		project.setCreatedDate(rs.getDate("created_date").toString());
		project.setCreatedTime(rs.getDate("created_time").toString());
		project.setDaysPassed(rs.getInt("days_passed"));		
		return project;
	}

}

package com.ripon.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ripon.entity.User;

public class UserMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setPhone(rs.getString("id"));
		user.setFname(rs.getString("fname"));
		user.setLname(rs.getString("lname"));
		user.setEmail(rs.getString("email"));
		user.setRole(rs.getString("role"));
		user.setImage("/profile-image/" + rs.getString("image"));
		return user;
	}

}

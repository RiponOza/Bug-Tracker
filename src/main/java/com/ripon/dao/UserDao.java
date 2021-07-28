package com.ripon.dao;

import java.util.Map;

import javax.sql.RowSet;
import javax.sql.rowset.RowSetProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ripon.entity.User;

@Repository
public class UserDao {

	@Autowired
	NamedParameterJdbcTemplate template;
	@Autowired
	JdbcTemplate jt;
	
	// save user data to database
	public boolean saveUser(User user) {
			String sqlquery = "INSERT INTO User(id, fname, lname, email, role) values(:id, :fname, :lname, :email, :role)";
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id", user.getPhone());
			params.addValue("fname", user.getFname());
			params.addValue("lname", user.getLname());
			params.addValue("email", user.getEmail());
			params.addValue("role", user.getRole());
			int status = template.update(sqlquery, params);
			return (status > 0 ? true : false);
	}
	
	// get password of user
	public String getUserPassword(String userEmail) {
		String sql = "SELECT password FROM Login WHERE email = ?;";
		try {
			return jt.queryForObject(sql, String.class, userEmail);
		}catch (Exception e) {
			return null;
		}
	}
	
	public boolean saveLoginDetail(String email, String password) {
		String sql = "INSERT INTO Login (email, password) VALUES(?, ?);";
		int rowCount = jt.update(sql, email, password);
		return (rowCount == 1)?true:false;
	}
		
	
	public User getUser(String id) {
		try {
			String sql = "SELECT id, fname, lname, email, role, image FROM User WHERE id=?";
			Map<String, Object> map = jt.queryForMap(sql, new Object[] {id});
			User user = new User();
			user.setPhone(map.get("id").toString());
			user.setFname(map.get("fname").toString());
			user.setLname(map.get("lname").toString());
			user.setEmail(map.get("email").toString());
			user.setRole(map.get("role").toString());
			user.setImage(map.get("image").toString());
			user.setPassword(null);
			user.setConfirmPassword(null);
			return user;
		} catch(Exception e) {
			return null;
		}
		
	}
	
	public User getUserByEmail(String email) {
		try {
			String sql = "SELECT id, fname, lname, email, role, image FROM User WHERE email=?";
			Map<String, Object> map = jt.queryForMap(sql, new Object[] {email});
			User user = new User();
			user.setPhone(map.get("id").toString());
			user.setFname(map.get("fname").toString());
			user.setLname(map.get("lname").toString());
			user.setEmail(map.get("email").toString());
			user.setRole(map.get("role").toString());
			user.setImage(map.get("image").toString());
			user.setPassword(null);
			user.setConfirmPassword(null);
			return user;
		} catch(Exception e) {
			return null;
		}
		
	}
	
	public boolean isEmailAvailable(String email) {
		try {
			String sql = "SELECT count(email) FROM User WHERE email=?;";
			return jt.queryForObject(sql, Integer.class, email)==0?false:true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean updateUser(User user) {
		try {
			String sql = "UPDATE User SET fname=?, lname=?, image=? WHERE id = ?";
			int rowEffected = jt.update(sql, user.getFname(), user.getLname(),user.getImage(), user.getPhone());
			return (rowEffected>0);
		} catch(Exception e) {
			return false;
		}
		
	}
	
	
	public boolean updatePassword(String email, String password) {
		try {
			String sql = "UPDATE Login SET password=? WHERE email = ?";
			int rowEffected = jt.update(sql, password, email);
			return (rowEffected>0);
		} catch(Exception e) {
			return false;
		}
		
		
	}
	

}

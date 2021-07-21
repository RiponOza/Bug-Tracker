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
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public boolean saveUser(User user) {
		String sqlquery = "INSERT INTO User(id, fname, lname, email, role, password) values(:id, :fname, :lname, :email, :role, :password)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", user.getPhone());
		params.addValue("fname", user.getFname());
		params.addValue("lname", user.getLname());
		params.addValue("email", user.getEmail());
		params.addValue("role", user.getRole());
		params.addValue("password", user.getPassword());

		int status = template.update(sqlquery, params);
		return (status > 0 ? true : false);
		
	}

	
	// fetch user of given detail
	public User getUser(String id, String password) {
		try {
			String sql = "SELECT id, fname, lname, email, role, image FROM User WHERE id=? AND password=?";
			Map<String, Object> map = jt.queryForMap(sql, new Object[] {id, password});
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
	
	
	public boolean updatePassword(String id, String password) {
		try {
			String sql = "UPDATE User SET password=? WHERE id = ?";
			int rowEffected = jt.update(sql, password, id);
			return (rowEffected>0);
		} catch(Exception e) {
			return false;
		}
		
		
	}
	

}

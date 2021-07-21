package com.ripon.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ripon.entity.Project;
import com.ripon.entity.User;

@Repository
public class ProjectDao {

	@Autowired
	private JdbcTemplate jt;

	@Autowired
	private UserDao userDao;

	private final String save_query = "INSERT INTO Project(title, descr, created, admin_id) VALUES(?,?,current_timestamp(),?)";
	private final String get_admin_projects = "SELECT id, title, descr, date_format(date(created), '%d-%M-%Y') as created_date, time_format(time(created), '%h:%i %p') as created_time, datediff(current_date(), date(created)) as days_passed, manager_id, admin_id FROM Project WHERE admin_id=? ORDER BY created DESC;";
	private final String get_query = "SELECT id, title, descr, date_format(date(created), '%d-%M-%Y') as created_date, time_format(time(created), '%h:%i %p') as created_time, datediff(current_date(), date(created)) as days_passed, admin_id, manager_id FROM Project WHERE id=? ORDER BY created DESC;";
	private final String update_query = "UPDATE Project SET id=?, title=?, descr=?, manager_id=? WHERE id=?;";
	private final String delete_query = "DELETE FROM Project WHERE id = ?";
	
	public boolean saveProject(Project project) {
		int rowCount = jt.update(save_query,
				new Object[] { project.getTitle(), project.getDesc(), project.getAdminId() });
		return (rowCount > 0);
	}

	
	
	public Project getProject(String projectId) {
		try {
			Map<String, Object> map = jt.queryForMap(get_query, projectId);
			Project project = new Project();
			project.setId(map.get("id").toString());
			project.setCreatedDate(map.get("created_date").toString());
			project.setCreatedTime(map.get("created_time").toString());
			project.setTitle(map.get("title").toString());
			project.setDesc(map.get("descr").toString());
			project.setDaysPassed(Long.parseLong(map.get("days_passed").toString()));
			if(map.get("manager_id")!=null) {
				project.setManagerId(map.get("manager_id").toString());
			}
			return project;
		}catch(Exception e) {
			return null;
		}
	}
	
	
	

	public List<Project> getProjectsOfAdmin(String adminId) {
		try {
			List<Map<String, Object>> list = jt.queryForList(get_admin_projects, new Object[] { adminId });
			List<Project> projectList = new ArrayList<Project>();
			for (Map<String, Object> map : list) {
				Project project = new Project();
				project.setId(map.get("id").toString());
				project.setCreatedDate(map.get("created_date").toString());
				project.setCreatedTime(map.get("created_time").toString());
				project.setTitle(map.get("title").toString());
				project.setDesc(map.get("descr").toString());
				project.setDaysPassed(Long.parseLong(map.get("days_passed").toString()));
				project.setManagerId(null);
				project.setAdminId(map.get("admin_id").toString());
				User user=null;
				if(map.get("manager_id")!=null) {
					user = userDao.getUser(map.get("manager_id").toString());
				}
				if(user!=null) {
					String projectManager = user.getFname() + " " + user.getLname();
					project.setProjectManager(projectManager);
				}
				projectList.add(project);
			}
			return projectList;
		} catch(Exception e) {
			return null;
		}
	}
	
	
	
	public Project getProjectOfManager(String managerId) {
		String query = "SELECT id, title, descr, date_format(date(created), '%d-%M-%Y') as created_date, time_format(time(created), '%h:%i %p') as created_time, datediff(current_date(), date(created)) as days_passed, manager_id, admin_id FROM Project WHERE manager_id=?;";
		try {
			Map<String, Object> map = jt.queryForMap(query, managerId);
			Project project = new Project();
			project.setId(map.get("id").toString());
			project.setCreatedDate(map.get("created_date").toString());
			project.setCreatedTime(map.get("created_time").toString());
			project.setTitle(map.get("title").toString());
			project.setDesc(map.get("descr").toString());
			project.setDaysPassed(Long.parseLong(map.get("days_passed").toString()));
			if(map.get("manager_id")!=null) {
				project.setManagerId(map.get("manager_id").toString());
			}
			return project;
		}catch(DataAccessException e) {
			return null;
		}
	}
	
	
	public Project getProjectOfUser(String userId) {
		String query = "SELECT project_id from Project_User where user_id=?;"; 
		try {
			String projectId = jt.queryForObject(query, String.class, userId);
			Project project = getProject(projectId);
			return project;
		}catch(Exception e) {
			return null;
		}
	}

	
	
	public boolean deleteProject(String projectId) {
		return jt.update(delete_query, projectId)==1?true:false;
	}

	
	public boolean updateProject(Project project) {
		int rowEffected = jt.update(update_query, project.getId(), project.getTitle(), project.getDesc(), project.getManagerId(), project.getId());
		return (rowEffected>0)?true:false;
	}
	
	
	public boolean isManagerFree(String managerId) {
		try {
			String sql = "SELECT count(id) FROM Project WHERE manager_id = ?";
			return jt.queryForObject(sql, Integer.class, managerId)==0?true:false;
		}catch (Exception e) {
			return false;
		}
	}
	
	
	public boolean addUserToProject(String userId, String projectId) {
		String sql = "INSERT INTO Project_User (user_id, project_id) VALUES(?, ?);";
		int rowUpdate = jt.update(sql, userId, projectId);
		return rowUpdate==1?true:false;
	}
	
	
	public boolean removeUserFromProject(String userId, String projectId) {
		String sql = "DELETE FROM Project_User WHERE user_id=? AND project_id=?;";
		int rowUpdate = jt.update(sql, userId, projectId);
		return rowUpdate==1?true:false;
	}
	
//	public List<User> getUsersOfProject(String projectId){
//		String sql = "SELECT id, fname, lname, email, role, image FROM User WHERE id IN (SELECT user_id FROM Project_User WHERE project_id = ?);";
//	}
	

}

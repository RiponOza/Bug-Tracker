package com.ripon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ripon.dao.ProjectDao;
import com.ripon.entity.Project;
import com.ripon.entity.Ticket;
import com.ripon.entity.User;
import com.ripon.rowMapper.UserMapper;

@Service
public class ProjectService {

	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private UserService userService;
	@Autowired
	private TicketService ticketService;

	public Long generateProjectId() {
		return 1L;
	}
	
	
	public boolean saveProject(Project project) {
		return projectDao.saveProject(project);
	}
	
	public Project getProject(String projectId) {
		return projectDao.getProject(projectId);
	}
	
	public Project getProjectOfManager(String managerId) {
		return projectDao.getProjectOfManager(managerId);
	}
	
	public List<Project> getProjectsOfAdmin(String adminId){
		return projectDao.getProjectsOfAdmin(adminId);
	}
	
	// gives project assigned to a dev/tester
	public Project getProjectOfUser(String userId) {
		return projectDao.getProjectOfUser(userId);
	}

	@Transactional
	public boolean deleteProject(String projectId) {
		ticketService.deleteTicketsOfProject(projectId);
		return projectDao.deleteProject(projectId);
	}

	public boolean updateProject(Project project) {
		return projectDao.updateProject(project);
	}
	
	public boolean assignProject(String projectId, String managerId) {
		Project project = getProject(projectId);
		if(project != null) {
			ticketService.assignTicketOfProjectManager(managerId, projectId);
			project.setManagerId(managerId);
			updateProject(project);
		}
		return false;
	}
	
	public boolean unassignProject(String projectId) {
		Project project = getProject(projectId);
		if(project != null) {
			ticketService.unassignTicketOfProjectManager(project.getManagerId(), project.getId());
			project.setManagerId(null);
			updateProject(project);
			return true;
		}
		return false;
	}

	public boolean isManagerFree(String managerId) {
		return projectDao.isManagerFree(managerId);
	}
	
	// assigns users to a project
	public boolean addUserToProject(String userId, String projectId) {
		return projectDao.addUserToProject(userId, projectId);
	}
	
	// removes users from a project
	public boolean removeUserFromProject(String userId, String projectId) {
		projectDao.unassignUserFromAllTickets(userId);
		return projectDao.removeUserFromProject(userId, projectId);
	}
	
	
	// gives list of users assigned to a project
	public List<User> getUsersOfProject(String projectId){
		return projectDao.getUsersOfProject(projectId);
	}
	

}

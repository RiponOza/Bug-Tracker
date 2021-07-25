package com.ripon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ripon.dao.TicketDao;
import com.ripon.entity.Project;
import com.ripon.entity.Ticket;

@Service
public class TicketService {
	
	@Autowired
	private TicketDao ticketDao;
	@Autowired
	private ProjectService projectService;
	
	
	public boolean saveTicket(Ticket ticket) {
		return ticketDao.saveTicket(ticket);
	}
	
	public Ticket getTicket(String ticketId) {
		return ticketDao.getTicket(ticketId);
	}
	
	public List<Ticket> getTicketsOfUser(String userId) {
		return ticketDao.getTicketsOfUser(userId);
	}
	
	public List<Ticket> getTicketsOfProjectManager(String managerId) {
		return ticketDao.getTicketsOfProjectManager(managerId);
	}
	
	public List<Ticket> getTicketsOfProject(String projectId) {
		return ticketDao.getTicketsOfProject(projectId);
	}
	
	public boolean assignTicketOfProjectManager(String managerId, String projectId) {
		return ticketDao.assignTicketOfProjectManager(managerId, projectId);
	}
	
	public boolean unassignTicketOfProjectManager(String managerId, String projectId) {
		return ticketDao.unassignTicketOfProjectManager(managerId, projectId);
	}
	
	// checks whether a user is assigned to ticket or not
	public boolean doesUserHasTicket(String userid) {
		return ((ticketDao.getTicketsOfUser(userid)==null)?false:true);
	}
	
	// assign users to ticket
	public boolean assignTicketToUser(String userid) {
		return false;
	}
	
	// unassign user of ticket
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public boolean unassignTicketOfUser(String userid) {
		return false;
	}
	
	public boolean deleteTicket(String ticketId) {
		return ticketDao.deleteTicket(ticketId);
	}
	
	// delete tickets of a project
	public boolean deleteTicketsOfProject(String projectId) {
		return ticketDao.deleteTicketsOfProject(projectId);
	}
}
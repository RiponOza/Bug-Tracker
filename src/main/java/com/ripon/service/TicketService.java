package com.ripon.service;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ripon.dao.TicketDao;
import com.ripon.entity.Project;
import com.ripon.entity.Ticket;
import com.ripon.entity.TicketResource;

@Service
public class TicketService {
	
	@Autowired
	private TicketDao ticketDao;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private TicketResourceService ticketResourceService;
	
	
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
	
	// assign ticket to a user
	public boolean assignTicketToUser(String userId, String ticketId) {
		try {
			ticketDao.assignTicketToUser(userId, ticketId);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// unassign a user from a ticket
	public boolean unassignTicketOfUser(String ticketId) {
		try {
			ticketDao.unassignTicketToUser(ticketId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	// checks whether a user is assigned to ticket or not
	public boolean doesUserHasTicket(String userid) {
		return ((ticketDao.getTicketsOfUser(userid)==null)?false:true);
	}
	
//	// assign users to ticket
//	@Transactional(isolation = Isolation.REPEATABLE_READ)
//	public boolean assignTicketToUser(String userid) {
//		return false;
//	}
	
	
	// delete a ticket and its related resources
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public boolean deleteTicket(String ticketId) {
		List<TicketResource> ticketResources = ticketResourceService.getTicketResources(ticketId);
		boolean status = false;
		if(ticketResources!=null) {
			ticketResourceService.deleteTicketResources(ticketId);
		}
		return ticketDao.deleteTicket(ticketId);

	}
	
	
	public boolean setTicketStatus(String ticketId, String newStatus) {
		return ticketDao.setTicketStatus(ticketId, newStatus);
	}
	
	// delete tickets of a project
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public boolean deleteTicketsOfProject(String projectId) {
		return ticketDao.deleteTicketsOfProject(projectId);
	}
	
	
	
	
	
}
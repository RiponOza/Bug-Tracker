package com.ripon.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ripon.entity.Ticket;
import com.ripon.rowMapper.TicketMapper;

@Repository
public class TicketDao {

	@Autowired
	private JdbcTemplate jt;

	private final String save_ticket_query = "INSERT INTO Ticket(title, descr, project_id, manager_id, status, type, priority, created_date, created_time, last_updated_date, last_updated_time) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private final String get_ticket_query = "SELECT id, title, descr, status, priority, type,  assigned_user_id, project_id, manager_id, date_format(created_date, '%d-%M-%Y') as created_date, time_format(created_time, '%h:%i %p') as created_time, date_format(last_updated_date, '%d-%M-%Y') as last_updated_date, time_format(last_updated_time, '%h:%i %p') as last_updated_time FROM Ticket WHERE id =?;";
	private final String get_ticket_manager_query = "SELECT id, title, descr, status, priority, type,  assigned_user_id, project_id, manager_id, date_format(created_date, '%d-%M-%Y') as created_date, time_format(created_time, '%h:%i %p') as created_time, date_format(last_updated_date, '%d-%M-%Y') as last_updated_date, time_format(last_updated_time, '%h:%i %p') as last_updated_time FROM Ticket WHERE manager_id =? ORDER BY priority DESC;";
	private final String get_ticket_assigned_user_query = "SELECT id, title, descr, status, priority, type,  assigned_user_id, project_id, manager_id, date_format(created_date, '%d-%M-%Y') as created_date, time_format(created_time, '%h:%i %p') as created_time, date_format(last_updated_date, '%d-%M-%Y') as last_updated_date, time_format(last_updated_time, '%h:%i %p') as last_updated_time FROM Ticket WHERE assigned_user_id =? ORDER BY priority DESC;";
	private final String get_ticket_project_query = "SELECT id, title, descr, status, priority, type,  assigned_user_id, project_id, manager_id, created_date, created_time, last_updated_date, last_updated_time FROM Ticket WHERE project_id =? ORDER BY priority DESC?;";
	private final String update_ticket_query = "UPDATE Ticket SET id=?, title=?, descr=? , project_id=? , manager_id=?, assigned_user_id=?, status=?, type=?, priority=?, created_date=?, created_time=?, last_updated_date=?, last_updated_time=? WHERE id=?;";

	
	
	public boolean saveTicket(Ticket ticket) {
		int rowCount = jt.update(save_ticket_query,
				new Object[] { ticket.getTitle(), ticket.getDescr(), ticket.getProjectId(), ticket.getManagerId(),
						ticket.getStatus(), ticket.getType(), ticket.getPriority(), ticket.getCreatedDate(),
						ticket.getCreatedTime(), ticket.getLastUpdatedDate(), ticket.getLastUpdatedTime() });
		return (rowCount == 1 ? true : false);
	}

	
	
	
	
	public Ticket getTicket(String ticketId) {
		try {
			Ticket ticket = jt.queryForObject(get_ticket_query, new TicketMapper(), ticketId);
			return ticket;
		}catch(Exception e) {
			return null;
		}
	}
	
	
	
	

	public List<Ticket> getTicketsOfProject(String projectId) {
		try {
			List<Ticket> ticketList = jt.query(get_ticket_project_query, new TicketMapper(), projectId);
			return ticketList;
		} catch(Exception e) {
			return null;
		}
	}
	
	
	
	

	public List<Ticket> getTicketsOfProjectManager(String managerId) {
		try {
			List<Ticket> ticketList = jt.query(get_ticket_manager_query, new TicketMapper(), managerId);
			return ticketList;
		}catch(Exception e) {
			return null;
		}
	}
	
	
	

	public List<Ticket> getTicketsOfUser(String userId) {
		try {
			List<Ticket> ticketList = jt.query(get_ticket_assigned_user_query, new TicketMapper(), userId);
			return ticketList;
		} catch(Exception e) {
			return null;
		}
	}
	
	
	

	public boolean updateTicket(Ticket ticket) {
		int rowCount = jt.update(update_ticket_query,
				new Object[] { ticket.getId(), ticket.getTitle(), ticket.getDescr(), ticket.getProjectId(),
						ticket.getManagerId(), ticket.getAssignedUserId(),ticket.getStatus(), ticket.getType(), ticket.getPriority(),
						ticket.getCreatedDate(), ticket.getCreatedTime(), ticket.getLastUpdatedDate(),
						ticket.getLastUpdatedTime(), ticket.getId() });
		return (rowCount == 1) ? true : false;
	}
	
	
	
	
	// assign ticket to a user
	public boolean assignTicketToUser(String userId, String ticketId) {
		try {
			String sql = "UPDATE Ticket SET assigned_user_id = ? WHERE id =?;";
			return ((jt.update(sql, userId, ticketId)==1)?true:false);
		}catch (Exception e) {
			return false;
		}
		
	}
	
	// unassign ticket to a user
	public boolean unassignTicketToUser(String ticketId) {
		try {
			String sql = "UPDATE Ticket SET assigned_user_id = null WHERE id = ?;";
			return ((jt.update(sql, ticketId) == 1) ? true : false);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	public boolean unassignTicketOfProjectManager(String managerId, String projectId) {
		String sql = "UPDATE Ticket SET manager_id = null WHERE manager_id = ? AND project_id=?;";
		int rowEffected = jt.update(sql, managerId, projectId);
		return rowEffected >= 1 ? true : false;
	}
	
	
	public boolean assignTicketOfProjectManager(String managerId, String projectId) {
		String sql = "UPDATE Ticket SET manager_id = ? WHERE project_id=?;";
		int rowEffected = jt.update(sql, managerId, projectId);
		return rowEffected >= 1 ? true : false;
	}
	
	
	public boolean setTicketStatus(String ticketId, String newStatus) {
		try {
			String sql = "UPDATE Ticket SET status = ? WHERE id = ?";
			return jt.update(sql, newStatus, ticketId)==1?true:false;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	

	public boolean deleteTicket(String ticketId) {
		String query = "DELETE FROM Ticket WHERE id = ?";
		return (jt.update(query, ticketId) == 1) ? true : false;
	}


	
	
	public boolean deleteTicketsOfProject(String projectId) {
		String query = "DELETE FROM Ticket WHERE project_id=?";
		return (jt.update(query, projectId) >= 1 ? true : false);
	}

}

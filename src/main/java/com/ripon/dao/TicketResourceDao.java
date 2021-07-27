package com.ripon.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ripon.entity.TicketResource;
import com.ripon.rowMapper.TicketResourceMapper;

@Repository
public class TicketResourceDao {
	
	@Autowired
	private JdbcTemplate jt;
	
	// saves ticket resource details
	public boolean saveTicketResourceDetail(TicketResource resource) {
		try {
			String sql = "INSERT INTO Ticket_Resource(ticket_id, resource_name, resource_type, created) VALUES(?,?,?,?);";
			int rowEffected = jt.update(sql, resource.getTicketId(), resource.getResourceName(), resource.getResourceType(), resource.getCreated());
			if(rowEffected==1) {
				return true;
			} else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

	// get resources of a ticket
	public List<TicketResource> getTicketResources(String ticketId){
		try {
			String sql = "SELECT id, ticket_id, resource_name, resource_type, created FROM Ticket_Resource WHERE ticket_id=?;";
			List<TicketResource> resources = jt.query(sql, new TicketResourceMapper(), ticketId);
			return resources;
		}catch (Exception e) {
			System.err.println("inside getTicketResource(String ticketId");
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean deleteTicketResource(String resourceId) {
		try {
			String sql = "DELETE FROM Ticket_Resource WHERE id = ?;";
			return jt.update(sql, resourceId)==1?true:false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
}

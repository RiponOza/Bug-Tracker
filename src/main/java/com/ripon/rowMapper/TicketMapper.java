package com.ripon.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ripon.entity.Ticket;

public class TicketMapper implements RowMapper<Ticket> {

	@Override
	public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
		Ticket ticket = new Ticket();
		ticket.setId(rs.getLong("id"));
		ticket.setTitle(rs.getString("title"));
		ticket.setDescr(rs.getString("descr"));
		ticket.setStatus(rs.getString("status"));
		ticket.setPriority(Ticket.PRIORITY.get(rs.getInt("priority")).toString());
		ticket.setType(rs.getString("type"));
		ticket.setAssignedUserId(rs.getLong("assigned_user_id"));
		ticket.setManagerId(rs.getLong("manager_id"));
		ticket.setProjectId(rs.getLong("project_id"));
		ticket.setCreatedDate(rs.getString("created_date")+"");
		ticket.setCreatedTime(rs.getString("created_time")+"");
		ticket.setLastUpdatedDate(rs.getString("last_updated_date")+"");
		ticket.setLastUpdatedTime(rs.getString("last_updated_time")+"");
		return ticket;
	}

}

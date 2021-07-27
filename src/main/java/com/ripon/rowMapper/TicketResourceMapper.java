package com.ripon.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ripon.entity.TicketResource;

public class TicketResourceMapper implements RowMapper<TicketResource> {

	@Override
	public TicketResource mapRow(ResultSet rs, int rowNum) throws SQLException {
		TicketResource resource = new TicketResource();
		resource.setId(rs.getString("id"));
		resource.setticketId(rs.getString("ticket_id"));
		resource.setResourceName(rs.getString("resource_name"));
		resource.setResourceType(rs.getString("resource_type"));
		resource.setCreated(rs.getString("created"));
		return resource;
	}
	
}

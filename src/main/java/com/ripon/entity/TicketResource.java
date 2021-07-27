package com.ripon.entity;

import org.springframework.stereotype.Component;

@Component
public class TicketResource {
	
	private String id;
	private String ticketId;
	private String resourceName;
	private String resourceType;
	private String created;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTicketId() {
		return ticketId;
	}
	public void setticketId(String ticketId) {
		this.ticketId = ticketId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	@Override
	public String toString() {
		return "TicketResource [id=" + id + ", ticketId=" + ticketId + ", resourceName=" + resourceName
				+ ", resourceType=" + resourceType + ", created=" + created + "]";
	}
	
	

}

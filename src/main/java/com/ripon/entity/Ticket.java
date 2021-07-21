package com.ripon.entity;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Ticket {
	private long id;
	@Size(min = 2, max = 200, message = "Characters must be more than 1 and less than 200 !")
	private String title;
	@Size(min = 0, max = 1000, message = "Maximum limit is 1000 characters !")
	private String descr;
	private long assignedUserId;
	private long projectId;
	private long managerId;
	private String status;
	@NotNull(message = "Priority can't be empty")
	private String priority;
	@NotBlank(message = "Type can't be blank !")
	private String type;
	private String createdDate;
	private String createdTime;
	private String lastUpdatedDate;
	private String lastUpdatedTime;
	
	public static final Map<Integer, String> PRIORITY = new HashMap<Integer, String>();
	
	public Ticket() {
		PRIORITY.put(0, "None");
		PRIORITY.put(1, "Low");
		PRIORITY.put(2, "Medium");
		PRIORITY.put(3, "High");
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public long getAssignedUserId() {
		return assignedUserId;
	}
	public void setAssignedUserId(long assignedUserId) {
		this.assignedUserId = assignedUserId;
	}
	public long getProjectId() {
		return projectId;
	}
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	public long getManagerId() {
		return managerId;
	}
	public void setManagerId(long managerId) {
		this.managerId = managerId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getLastUpdatedTime() {
		return lastUpdatedTime;
	}
	public void setLastUpdatedTime(String lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}
	@Override
	public String toString() {
		return "Ticket [id=" + id + ", title=" + title + ", descr=" + descr + ", assignedUserId=" + assignedUserId
				+ ", projectId=" + projectId + ", managerId=" + managerId + ", status=" + status + ", priority="
				+ priority + ", type=" + type + ", createdDate=" + createdDate + ", createdTime=" + createdTime
				+ ", lastUpdatedDate=" + lastUpdatedDate + ", lastUpdatedTime=" + lastUpdatedTime + "]";
	}


}
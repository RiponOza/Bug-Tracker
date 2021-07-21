package com.ripon.entity;

import org.springframework.stereotype.Component;

@Component
public class Project {
	private String id;
	private String title;
	private String desc;
	private String managerId; // this is user id
	private String adminId;
	private String createdDate;
	private String createdTime;
	private long daysPassed;
	private String projectManager;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
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

	public long getDaysPassed() {
		return daysPassed;
	}

	public void setDaysPassed(long daysPassed) {
		this.daysPassed = daysPassed;
	}
	
	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}
	
	public String getProjectManager() {
		return projectManager;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", title=" + title + ", desc=" + desc + ", managerId=" + managerId + ", adminId="
				+ adminId + ", " + ", createdDate=" + createdDate + ", createdTime=" + createdTime
				+ ", daysPassed=" + daysPassed + "]";
	}

}

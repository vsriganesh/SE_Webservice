package com.iiitb.tr.workflow.dao;

import java.io.Serializable;

public class UserVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int userId;
	private String userName;
	private String email;
	private String role;
	
	
	public UserVo() {
		
	}
	
	public UserVo(int userId) {
		this.userId = userId;
	}
	
	
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		if(((UserVo)o).getUserId() == this.userId)
			return true;
		else
			return false;
	}
	
	

}

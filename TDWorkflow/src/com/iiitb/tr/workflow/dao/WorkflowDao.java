package com.iiitb.tr.workflow.dao;

import java.util.ArrayList;

public interface WorkflowDao {

	public ArrayList<String> getAllUsers();

	public UserVo authenticateUser(String authToken);
	
	public ArrayList<String> trList();
	
	public void newTrCreation();
	
	public String getTrId();
	
}

package com.iiitb.tr.workflow.dao;

import java.sql.Date;
import java.util.ArrayList;

public interface WorkflowDao {

	public ArrayList<String> getAllUsers();

	public UserVo authenticateUser(String authToken);
	
	public ArrayList<Object> trList(String role);
	
	public int createUser(String userName,String password,String email,String role);
	
	public int deleteUser(String userName);
	
	public int updateUser(String userName,String email,String role);
	
	
	public String newTrCreation(String fileName,UserVo vo);
	
	public String getTrId(String fileName);
	
	public TrDocumentVo getTrDetails(String trId);
	
	
	public int docAuthUpdate(String trId,String userId);
	
	public int deleteTrDocument(String trId);
	
	
	
	public int docAuthDeletion(String trId);
	
	public int docReviewDeletion(String trId);
	
	
	
}

package com.iiitb.tr.workflow.dao;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class TrDocumentVo implements Serializable {
		
	
	private static final long serialVersionUID = 1L;
	
	
		int documentId;
		List<String> authorList;
		public List<UserVo> getAuthors() {
			return authors;
		}
		public void setAuthors(List<UserVo> authors) {
			this.authors = authors;
		}
		public List<UserVo> getReviewers() {
			return reviewers;
		}
		public void setReviewers(List<UserVo> reviewers) {
			this.reviewers = reviewers;
		}
		List<String> reviewerList;
		
		List<UserVo> authors;
		List<UserVo> reviewers;
		
		
		String description;
		String currentState;
		Date creationDate;
		Date modifyDate;
		int reviewerCount;
		int currentCount;
		
		
		
		public int getReviewerCount() {
			return reviewerCount;
		}
		public void setReviewerCount(int reviewerCount) {
			this.reviewerCount = reviewerCount;
		}
		public int getCurrentCount() {
			return currentCount;
		}
		public void setCurrentCount(int currentCount) {
			this.currentCount = currentCount;
		}
		public List<String> getReviewerList() {
			return reviewerList;
		}
		public void setReviewerList(List<String> reviewerList) {
			this.reviewerList = reviewerList;
		}
		
		public int getDocumentId() {
			return documentId;
		}
		public void setDocumentId(int documentId) {
			this.documentId = documentId;
		}
		public List<String> getAuthList() {
			return authorList;
		}
		public void setAuthList(List<String> authorList) {
			this.authorList = authorList;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getCurrentState() {
			return currentState;
		}
		public void setCurrentState(String currentState) {
			this.currentState = currentState;
		}
		public Date getCreationDate() {
			return creationDate;
		}
		public void setCreation(Date creationDate) {
			this.creationDate = creationDate;
		}
		public Date getModifyDate() {
			return modifyDate;
		}
		public void setModifyDate(Date modifyDate) {
			this.modifyDate = modifyDate;
		}
		
	
}

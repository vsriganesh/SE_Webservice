package com.iiitb.tr.workflow.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


import com.iiitb.tr.workflow.util.ConnectionPool;

public class WorkflowDaoImpl implements WorkflowDao {
	
	
	public static final String trDocList = "select t.TrID,t.CreationDate,t.ModifyDate,t.Description,s.StateName from trdocument t,state s where t.StateID = s.StateID";
	

	@Override
	public ArrayList<String> getAllUsers() {
		// TODO Auto-generated method stub

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<String> retList = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			conn = ConnectionPool.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery("select * from user");
			UserVo vo;
			retList = new ArrayList<String>();
			while (rs.next()) {
				vo = new UserVo();

				vo.setUserId(rs.getInt("UserID"));

				vo.setUserName(rs.getString("UserName"));
				vo.setEmail(rs.getString("UserEmail"));
				vo.setRole(rs.getString("Role"));
				
				retList.add(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(vo));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				if (rs != null)
					rs.close();

				if (st != null)
					st.close();

				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return retList;
	}
	
	@Override
	public UserVo authenticateUser(String authToken) {
		// TODO Auto-generated method stub
		Connection conn = null;
		java.sql.PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVo vo= null;

		try {
			conn = ConnectionPool.getConnection();
			pstmt = conn.prepareStatement("select * from user where Password=?");
			pstmt.setString(1, authToken);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				vo = new UserVo();

				vo.setUserId(rs.getInt("UserID"));

				vo.setUserName(rs.getString("UserName"));
				vo.setEmail(rs.getString("UserEmail"));
				vo.setRole(rs.getString("Role"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				if (rs != null)
					rs.close();

				if (pstmt != null)
					pstmt.close();

				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return vo;

	}

	@Override
	public ArrayList<String> trList() {
		Connection conn = null;
		Statement pstmt = null;
		Statement pstmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		TrDocumentVo vo= null;
		List<String> authList =null;
		List<String> retList = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			conn = ConnectionPool.getConnection();
			pstmt = conn.createStatement();
			pstmt1 = conn.createStatement();
			rs = pstmt.executeQuery(trDocList);
			retList = new ArrayList<String>();
			
			while (rs.next()) {
				vo = new TrDocumentVo();
				authList = new ArrayList<String>();
				vo.setDocumentId(rs.getInt("TrID"));
				
				rs1 =  pstmt1.executeQuery("select u.UserName from user u , doc_auth d where d.trID="+rs.getInt("TrID")+" and d.UserID = u.UserID");
				while (rs1.next()) 
				{
					authList.add(rs1.getString("UserName"));
				}
				
			
				
				vo.setAuthList(authList);
				vo.setCreation(rs.getDate("CreationDate"));
				vo.setModifyDate(rs.getDate("ModifyDate"));
				vo.setDescription(rs.getString("Description"));
				vo.setCurrentState(rs.getString("StateName"));
				
				retList.add(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(vo));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				if(rs1!=null)
					rs1.close();
				
				if (rs != null)
					rs.close();

				if (pstmt1 != null)
					pstmt1.close();
				
				if (pstmt != null)
					pstmt.close();

				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ((ArrayList<String>)retList);
	}

	@Override
	public void newTrCreation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTrId() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
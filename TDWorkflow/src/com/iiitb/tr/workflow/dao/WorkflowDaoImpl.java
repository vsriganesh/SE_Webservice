package com.iiitb.tr.workflow.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import java.util.Date;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;















import com.iiitb.tr.workflow.util.ConnectionPool;
import com.iiitb.tr.workflow.util.Constants;

public class WorkflowDaoImpl implements WorkflowDao {
	
	
	public static final String trDocList = "select t.TrID,t.CreationDate,t.ModifyDate,t.Description,s.StateName from trdocument t,state s where t.StateID = s.StateID";
	public static final String userCreation = "insert into user (UserName,UserEmail,Password,Role) values (?,?,?,?)";
	public static final String userDeletion = "delete from user where UserName =?";
	public static final String userEmailUpdation = "update user set UserEmail =? where UserName =?";
	public static final String userRoleUpdation = "update user set Role =? where UserName =?";
	public static final String userUpdation = "update user set UserEmail =? and Role =? where UserName =?";
	public static final String trCreation = "insert into trdocument (CreationDate,ModifyDate,StateID,Description,ReviewerCount,CurrentCount) values (?,?,?,?,?,?)";
	public static final String trId = "select TrID from trdocument where Description = ?";
	public static final String TR_DETAILS= "select t.TrID,t.CreationDate,t.ModifyDate,t.Description,s.StateName from trdocument t,state s where t.StateID = s.StateID and t.TrID=?";
	public static final String DOC_AUTH_UPDATE = "insert into doc_auth (TrID,UserID) values (?,?)";
	
	public static final String TrDocDeletion = "delete from trdocument where TrID =?";
	public static final String TrDocAuthDeletion = "delete from doc_auth where TrID =?";
	public static final String TrDocReviewDeletion = "delete from doc_review where TrID =?";
	
	
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
	public ArrayList<Object> trList(String role) {
		Connection conn = null;
		Statement pstmt = null;
		Statement pstmt1 = null;
		Statement pstmt2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		TrDocumentVo vo= null;
		List<String> authList =null;
		List<String> reviewerList =null;
		List<Object> retListAdmin = null;
		List<Object> retListNormal = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			conn = ConnectionPool.getConnection();
			pstmt = conn.createStatement();
			pstmt1 = conn.createStatement();
			pstmt2 = conn.createStatement();
			
			rs = pstmt.executeQuery(trDocList);
			retListAdmin = new ArrayList<Object>();
			retListNormal = new ArrayList<Object>();
			reviewerList=new ArrayList<String>();;
			
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
				
				rs2 =  pstmt2.executeQuery("select u.UserName from user u , doc_review d where d.trID="+rs.getInt("TrID")+" and d.UserID = u.UserID");
				while (rs2.next()) 
				{
					reviewerList.add(rs2.getString("UserName"));
				}
				vo.setReviewerList(reviewerList);
				
				vo.setCreation(rs.getDate("CreationDate"));
				vo.setModifyDate(rs.getDate("ModifyDate"));
				vo.setDescription(rs.getString("Description"));
				vo.setCurrentState(rs.getString("StateName"));
				if(role.equalsIgnoreCase(Constants.ADMIN))
				retListAdmin.add(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(vo));
				else
					retListNormal.add(vo);
					
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
				if(rs2!=null)
					rs1.close();
				
				if(rs1!=null)
					rs1.close();
				
				if (rs != null)
					rs.close();

				if (pstmt2 != null)
					pstmt1.close();
				
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

		if(role.equalsIgnoreCase(Constants.ADMIN))
		return ((ArrayList<Object>)retListAdmin);
		else
			return ((ArrayList<Object>)retListNormal);
	}

	@Override
	public String newTrCreation(String fileName,UserVo vo) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement pstmt = null;
		int retVal=0;
		String trId=null;

		try {
			conn = ConnectionPool.getConnection();
			pstmt = conn.prepareStatement(trCreation);
			Date dNow = new Date( );
		      SimpleDateFormat ft = 
		      new SimpleDateFormat ("yyyy/M/dd");
		     
		    pstmt.setString(1,ft.format(dNow));
			pstmt.setString(2, ft.format(dNow));
			pstmt.setInt(3, 1);
			pstmt.setString(4, fileName);
			pstmt.setInt(5, 0);
			pstmt.setInt(6, 0);
			retVal = pstmt.executeUpdate();
			
			if(retVal!=0)
			{
				
				trId = getTrId(fileName);
				
			}
			
			
			if(trId!=null)
			{
				docAuthUpdate(trId,String.valueOf(vo.getUserId()));
				
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			try {
			
				
				if (pstmt != null)
					pstmt.close();

				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return trId;
		
		
	}

	@Override
	public String getTrId(String fileName) {
		// TODO Auto-generated method stub

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String retVal=null;

		try {
			conn = ConnectionPool.getConnection();
			pstmt = conn.prepareStatement(trId);
			pstmt.setString(1, fileName);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				retVal = rs.getString("TrID");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			try {
			
				if(rs!=null)
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
		return retVal;
	}

	@Override
	public int createUser(String userName, String password, String email,
			String role){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int retVal=0;

		try {
			conn = ConnectionPool.getConnection();
			pstmt = conn.prepareStatement(userCreation);
			pstmt.setString(1, userName);
			pstmt.setString(2, email);
			pstmt.setString(3, password);
			pstmt.setString(4, role);
			retVal = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			try {
			
				
				if (pstmt != null)
					pstmt.close();

				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retVal;
	}

	@Override
	public int deleteUser(String userName) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int retVal=0;

		try {
			conn = ConnectionPool.getConnection();
			pstmt = conn.prepareStatement(userDeletion);
			pstmt.setString(1, userName);
			
			retVal = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			try {
			
				
				if (pstmt != null)
					pstmt.close();

				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retVal;
	}

	@Override
	public int updateUser(String userName, String email, String role) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement pstmt = null;
		int retVal=0;

		try {
			conn = ConnectionPool.getConnection();
			
			if(email!=null && role!=null)
			{
			pstmt = conn.prepareStatement(userUpdation);
			pstmt.setString(1, email);
			pstmt.setString(2, role);
			pstmt.setString(3, userName);
			}
			
			else if(email!=null)
			{
			pstmt = conn.prepareStatement(userEmailUpdation);
			pstmt.setString(1, email);
			
			pstmt.setString(2, userName);
			}
			
			else
			{
				pstmt = conn.prepareStatement(userRoleUpdation);
				pstmt.setString(1, role);
				
				pstmt.setString(2, userName);
			}
			
			retVal = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			try {
			
				
				if (pstmt != null)
					pstmt.close();

				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retVal;
	
	}

	@Override
	public TrDocumentVo getTrDetails(String trId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		Statement pstmt1 = null;
		Statement pstmt2 = null;
		
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		
		TrDocumentVo vo= null;
		List<String> authList =null;
		List<String> reviewerList =null;
		List<String> retList = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			conn = ConnectionPool.getConnection();
			
			pstmt1 = conn.createStatement();
			pstmt2 = conn.createStatement();
			
			pstmt = conn.prepareStatement(TR_DETAILS);
			pstmt.setString(1, trId);
			rs = pstmt.executeQuery();
			retList = new ArrayList<String>();
			
			while (rs.next()) {
				vo = new TrDocumentVo();
				authList = new ArrayList<String>();
				reviewerList = new ArrayList<String>();
				
				vo.setDocumentId(rs.getInt("TrID"));
				
				rs1 =  pstmt1.executeQuery("select u.UserName from user u , doc_auth d where d.trID="+rs.getInt("TrID")+" and d.UserID = u.UserID");
				while (rs1.next()) 
				{
					authList.add(rs1.getString("UserName"));
				}
				vo.setAuthList(authList);
				
				rs2 =  pstmt2.executeQuery("select u.UserName from user u , doc_review d where d.trID="+rs.getInt("TrID")+" and d.UserID = u.UserID");
				while (rs2.next()) 
				{
					reviewerList.add(rs2.getString("UserName"));
				}
			
				vo.setReviewerList(reviewerList);
				
				vo.setCreation(rs.getDate("CreationDate"));
				vo.setModifyDate(rs.getDate("ModifyDate"));
				
				vo.setDescription(rs.getString("Description").split("\\.")[1]);
				vo.setCurrentState(rs.getString("StateName"));
				
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   finally {

			try {
				if(rs2!=null)
					rs1.close();
				
				if(rs1!=null)
					rs1.close();
				
				if (rs != null)
					rs.close();

				if (pstmt2 != null)
					pstmt1.close();
				
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

		return vo;
	}

	@Override
	public int docAuthUpdate(String trId, String userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int retVal=0;

		try {
			conn = ConnectionPool.getConnection();
			pstmt = conn.prepareStatement(DOC_AUTH_UPDATE);
			pstmt.setString(1, trId);
			pstmt.setString(2, userId);
			
			retVal = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			try {
			
				
				if (pstmt != null)
					pstmt.close();

				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retVal;
	}

	@Override
	public int deleteTrDocument(String trId) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement pstmt = null;
		int retVal=0;

		try {
			
			int temp = docAuthDeletion(trId);
			 docReviewDeletion(trId);
			
			
			// Document may or may not have reviewer
			 
			if(temp!=0)
			{
			conn = ConnectionPool.getConnection();
			pstmt = conn.prepareStatement(TrDocDeletion);
			pstmt.setString(1, trId);
			
			
			
			retVal = pstmt.executeUpdate();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			try {
			
				
				if (pstmt != null)
					pstmt.close();

				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retVal;
	}

	@Override
	public int docAuthDeletion(String trId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int retVal=0;

		try {
			conn = ConnectionPool.getConnection();
			pstmt = conn.prepareStatement(TrDocAuthDeletion);
			pstmt.setString(1, trId);
			
			retVal = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			try {
			
				
				if (pstmt != null)
					pstmt.close();

				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retVal;
	}

	@Override
	public int docReviewDeletion(String trId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int retVal=0;

		try {
			conn = ConnectionPool.getConnection();
			pstmt = conn.prepareStatement(TrDocReviewDeletion);
			pstmt.setString(1, trId);
			
			retVal = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			try {
			
				
				if (pstmt != null)
					pstmt.close();

				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retVal;
	}

	

}

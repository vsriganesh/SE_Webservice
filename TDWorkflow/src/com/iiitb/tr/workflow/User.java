package com.iiitb.tr.workflow;

import java.util.ArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;

import com.iiitb.tr.workflow.dao.UserVo;
import com.iiitb.tr.workflow.dao.WorkflowDao;
import com.iiitb.tr.workflow.dao.WorkflowDaoImpl;
import com.iiitb.tr.workflow.util.Constants;

@Path("/users")
public class User {

	@GET
	@Path("{auth}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllUsers(@PathParam("auth") String authToken) throws JSONException {

		WorkflowDao dao = new WorkflowDaoImpl();
		UserVo vo = dao.authenticateUser(authToken);
		
		if(vo!=null && vo.getRole().equalsIgnoreCase(Constants.ADMIN))
		{
		
		  dao = new WorkflowDaoImpl();
	
		  return dao.getAllUsers().toString();
		}
		else
			return "Sorry!!! You are not authorized to view the requested URI";
	}
	
	@PUT
	@Path("{auth}")
	@Produces(MediaType.TEXT_PLAIN)
	public String modifyUser(@PathParam("auth") String authToken) {

		WorkflowDao dao = new WorkflowDaoImpl();
		ArrayList<String> userList = dao.getAllUsers();

		// System.out.println(jb.toString());
		return userList.toString();

	}
	
	@POST
	@Path("{auth}")
	@Produces(MediaType.TEXT_PLAIN)
	public String createUser(@PathParam("auth") String authToken) {

		WorkflowDao dao = new WorkflowDaoImpl();
		ArrayList<String> userList = dao.getAllUsers();

		// System.out.println(jb.toString());
		return userList.toString();

	}
	
	
	@DELETE
	@Path("{auth}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteUser(@PathParam("auth") String authToken) {

		WorkflowDao dao = new WorkflowDaoImpl();
		ArrayList<String> userList = dao.getAllUsers();

		// System.out.println(jb.toString());
		return userList.toString();

	}

}

package com.iiitb.tr.workflow;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.iiitb.tr.workflow.dao.UserVo;
import com.iiitb.tr.workflow.dao.WorkflowDao;
import com.iiitb.tr.workflow.dao.WorkflowDaoImpl;
import com.iiitb.tr.workflow.util.Constants;
@Path("/reviewer")
public class Reviewer {

	@GET
	@Path("{auth}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getReviewerList(@PathParam("auth") String authToken) {

		WorkflowDao dao = new WorkflowDaoImpl();
		UserVo vo = dao.authenticateUser(authToken);

		if (vo != null && vo.getRole().equalsIgnoreCase(Constants.ADMIN)) {

			dao = new WorkflowDaoImpl();

			try {
				return dao.getReviewerDetails().toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			return "Sorry!!! You are not authorized to view the requested URI";
		
		
		return "Sorry!!! You are not authorized to view the requested URI";
	}
	
	
	
	
	
	@GET
	@Path("{auth}/{userid}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getReviewerListForDoc(@PathParam("auth") String authToken,@PathParam("userid") String reviewer) {

		WorkflowDao dao = new WorkflowDaoImpl();
		UserVo vo = dao.authenticateUser(authToken);

		if (vo != null && vo.getRole().equalsIgnoreCase(Constants.ADMIN)) {

			dao = new WorkflowDaoImpl();

			try {
				return dao.getReviewerDocDetails(reviewer).toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			return "Sorry!!! You are not authorized to view the requested URI";
		
		
		return "Sorry!!! You are not authorized to view the requested URI";
	}
	
	
}

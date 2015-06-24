package com.iiitb.tr.workflow;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
	@Path("/admin/{auth}")
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
			return Constants.INVALIDUSER;
		
		
		return Constants.INVALIDUSER;
	}
	
	
	
	
	
	@GET
	@Path("/admin/{auth}/{userid}")
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
			return Constants.INVALIDUSER;
		
		
		return Constants.INVALIDUSER;
	}
	
	
	
	@POST
	@Path("{auth}/accept/{TrID}")
	@Produces(MediaType.TEXT_PLAIN)
	
	/***********************************************************
	 *  This would make the issuer the reviewer of TrID
	 * @return Success message
	 */
	public String acceptTask(@PathParam("auth") String authToken,@PathParam("TrID") int TrId){
		String message = null;
		WorkflowDao dao=new WorkflowDaoImpl();
		UserVo user=dao.authenticateUser(authToken);
		if(user!=null){
		
		message=dao.acceptTask(user.getUserId(),TrId);
		return message;
		}
		else
			return Constants.INVALIDUSER;
		
	}
	
	
	@POST
	@Path("{auth}/reject/{TrID}")
	@Produces(MediaType.TEXT_PLAIN)
	
	/***********************************************************
	 *  This would make the issuer the reviewer of TrID
	 * @return Success message
	 */
	public String rejectTask(@PathParam("auth") String authToken,@PathParam("TrID") int TrId){
		String message = null;
		WorkflowDao dao=new WorkflowDaoImpl();
		UserVo user=dao.authenticateUser(authToken);
		if(user!=null){
		message=dao.rejectTask(user.getUserId(),TrId);
		return message;
		}
		else
			return Constants.INVALIDUSER;
		
	}
	
	
	@GET
	@Path("{auth}")
	@Produces(MediaType.TEXT_PLAIN)
	
	/***********************************************************
	 *  This would make the issuer the reviewer of TrID
	 * @return Success message
	 */
	public String showTasks(@PathParam("auth") String authToken){
		String message = null;
		WorkflowDao dao=new WorkflowDaoImpl();
		UserVo user=dao.authenticateUser(authToken);
		if(user!=null)
		{
			message=dao.showTasks(user.getUserId());
			return message;	
		}
		else
			return Constants.INVALIDUSER;
	}

	
	
}

package com.iiitb.tr.workflow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.iiitb.tr.workflow.dao.TrDocumentVo;
import com.iiitb.tr.workflow.dao.UserVo;
import com.iiitb.tr.workflow.dao.WorkflowDao;
import com.iiitb.tr.workflow.dao.WorkflowDaoImpl;
import com.iiitb.tr.workflow.util.Constants;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

@Path("/doc")
public class Document {
	private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C://Users/vsriganesh/Desktop/upload_files/";

	@GET
	@Path("{auth}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getTrDocList(@PathParam("auth") String auth) {
		WorkflowDao dao = new WorkflowDaoImpl();
		UserVo vo = dao.authenticateUser(auth);

		if (vo != null) {
				if(vo.getRole().equalsIgnoreCase(Constants.ADMIN))
			return (dao.trList(Constants.ADMIN).toString());
				else
				{
					ObjectMapper mapper = new ObjectMapper();
					List<String> retList = new ArrayList<String>();
					List<Object> tempList = new ArrayList<Object>();
					tempList = dao.trList(Constants.NORMAL);
					Iterator iter = tempList.iterator();
					TrDocumentVo temp;
					while(iter.hasNext())
					{
						temp = (TrDocumentVo)iter.next();
						if(temp.getAuthList().contains(vo.getUserName()) || temp.getReviewerList().contains(vo.getUserName()))
						{
							try {
								retList.add(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(temp));
							} catch (JsonGenerationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JsonMappingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					return retList.toString();
				}
		}

		else
			return "Sorry!!! You are not authorized to view the requested URI";
	}

	@GET
	@Path("{trId}/{auth}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getDoc(@PathParam("trId") String trId,
			@PathParam("auth") String auth) {
		WorkflowDao dao = new WorkflowDaoImpl();
		UserVo vo = dao.authenticateUser(auth);

		if (vo != null) {
		
			TrDocumentVo ret = dao.getTrDetails(trId);
			if(ret!=null && ((ret.getAuthList().contains(vo.getUserName())) || (ret.getReviewerList().contains(vo.getUserName()))) )
			{
		File file = new File("C:\\Users\\vsriganesh\\Desktop\\upload_files\\"+trId+"."+ret.getDescription());
		return Response
				.ok(file, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition",
						"attachment; filename=\"" + file.getName() + "\"") // optional
				.build();
	}
			else
				return Response.ok("Sorry!!! You are not authorized to view the requested URI",MediaType.TEXT_PLAIN).build();	
		}
		else
			return Response.ok("Sorry!!! You are not authorized to view the requested URI",MediaType.TEXT_PLAIN).build();
	}

	@PUT
	@Path("{rollNo}")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateDoc(@PathParam("rollNo") String rollNo) {
		return "updated";
	}

	/**
	 * 
	 * Upload a File
	 */

	@POST
	@Path("{auth}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	public String uploadFile(FormDataMultiPart form,
			@PathParam("auth") String auth) {

		WorkflowDao dao = new WorkflowDaoImpl();
		UserVo vo = dao.authenticateUser(auth);

		if (vo != null) {
			FormDataBodyPart filePart = form.getField("file");

			ContentDisposition headerOfFilePart = filePart
					.getContentDisposition();

			InputStream fileInputStream = filePart
					.getValueAs(InputStream.class);

			// Make DB entry

			String trId = dao.newTrCreation(headerOfFilePart.getFileName(),vo);

			// save the file to the server
			if (trId != null) {
				
				String format[] = headerOfFilePart.getFileName().split("\\.");
				
				System.out.println("format length "+format.length);
				
				String filePath;
				if (format.length == 2) {
					filePath = SERVER_UPLOAD_LOCATION_FOLDER + trId + "."
							+ format[1];
				} else
					filePath = SERVER_UPLOAD_LOCATION_FOLDER + trId;

				System.out.println("file path "+filePath);
				saveFile(fileInputStream, filePath);

				return "<html><body> <u><center>File :<b>"
						+ headerOfFilePart.getFileName()
						+ " </b>uploaded successfully</center></u></b> <br/><center> Document ID : <b>"+trId+"</b></center>  </body> </html>";
			}

			else
				return "<html><body> <u><center>File :<b>"
						+ headerOfFilePart.getFileName()
						+ " </b>upload failed</center></u></b> </body> </html>";
		} else
			return "Sorry!!! You are not authorized to view the requested URI";
	}

	// save uploaded file to a defined location on the server

	private void saveFile(InputStream uploadedInputStream,

	String serverLocation) {

		try {

			OutputStream outpuStream = new FileOutputStream(new File(
					serverLocation));

			int read = 0;

			byte[] bytes = new byte[1024];

			outpuStream = new FileOutputStream(new File(serverLocation));

			while ((read = uploadedInputStream.read(bytes)) != -1) {

				outpuStream.write(bytes, 0, read);
			}

			outpuStream.flush();

			outpuStream.close();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	@DELETE
	@Path("{trId}/{auth}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteDoc(@PathParam("trId") String trId,
			@PathParam("auth") String auth) {
		WorkflowDao dao = new WorkflowDaoImpl();
		UserVo vo = dao.authenticateUser(auth);

		if (vo != null) {
		
			TrDocumentVo ret = dao.getTrDetails(trId);
			if(ret!=null && ((ret.getAuthList().contains(vo.getUserName())) && (!ret.getCurrentState().equalsIgnoreCase(Constants.PUBLISHED))))
			{
					
					if(dao.deleteTrDocument(trId)!=0)
					{
					File file = new File("C:\\Users\\vsriganesh\\Desktop\\upload_files\\"+trId+"."+ret.getDescription());
					file.deleteOnExit();
					return "File deleted !!!!! ";
					}
					else
						return "Sorry!!!! Unable to delete file";
						
			}
			
			else
				return "Sorry!!! You are not authorized to view the requested URI";

			}
		else
			return "Sorry!!! You are not authorized to view the requested URI";
		
	}
	

}

/*
 * @GET
 * 
 * @Path("/{key}") public Response download(@PathParam("key") String key,
 * 
 * @Context HttpServletResponse response) throws IOException { try { //Get your
 * File or Object from wherever you want... //you can use the key parameter to
 * indentify your file //otherwise it can be removed //let's say your file is
 * called "object" response.setContentLength((int) object.getContentLength());
 * response.setHeader("Content-Disposition", "attachment; filename=" +
 * object.getName()); ServletOutputStream outStream =
 * response.getOutputStream(); byte[] bbuf = new byte[(int)
 * object.getContentLength() + 1024]; DataInputStream in = new DataInputStream(
 * object.getDataInputStream()); int length = 0; while ((in != null) && ((length
 * = in.read(bbuf)) != -1)) { outStream.write(bbuf, 0, length); } in.close();
 * outStream.flush(); } catch (S3ServiceException e) { e.printStackTrace(); }
 * catch (ServiceException e) { e.printStackTrace(); } return
 * Response.ok().build(); }
 */

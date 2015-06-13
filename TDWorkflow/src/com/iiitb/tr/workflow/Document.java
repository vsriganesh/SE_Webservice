package com.iiitb.tr.workflow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
	public String getTrDocList(@PathParam("auth") String auth)
	{
		WorkflowDao dao = new WorkflowDaoImpl();
		UserVo vo = dao.authenticateUser(auth);
		
		if(vo!=null && vo.getRole().equalsIgnoreCase(Constants.ADMIN))
		{
				
		
			return (dao.trList().toString());
		}
		
		else	
		return "Sorry!!! You are not authorized to view the requested URI";
	}
	
	
	@GET
	@Path("{trId}/{auth}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getDoc(@PathParam("trId") String trId,
			@PathParam("auth") String auth) {
		File file = new File("C:\\Users\\vsriganesh\\Desktop\\OS Processes.pdf");
		return Response
				.ok(file, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition",
						"attachment; filename=\"" + file.getName() + "\"") // optional
				.build();
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

		FormDataBodyPart filePart = form.getField("file");

		ContentDisposition headerOfFilePart = filePart.getContentDisposition();

		InputStream fileInputStream = filePart.getValueAs(InputStream.class);

		String filePath = SERVER_UPLOAD_LOCATION_FOLDER
				+ headerOfFilePart.getFileName();

		// save the file to the server

		saveFile(fileInputStream, filePath);

		
		
		return "<html><body> <b><u><center>File uploaded successfully</center></u></b> </body> </html>";

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
	@Path("{rollNo}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteDoc(@PathParam("rollNo") String rollNo) {
		return "updated";
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
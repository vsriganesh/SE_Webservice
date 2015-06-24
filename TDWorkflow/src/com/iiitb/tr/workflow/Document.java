package com.iiitb.tr.workflow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/doc")
public class Document {
	private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C://Users/vsriganesh/Desktop/upload_files/";

	@GET
	@Path("{auth}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getTrDocList(@PathParam("auth") String auth) {
		WorkflowDao dao = new WorkflowDaoImpl();
		UserVo vo = dao.authenticateUser(auth);
		System.out.println("vo "+vo);
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
			return Constants.INVALIDUSER;
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
				return Response.ok("Sorry!!! You are not authorized to view the requested document",MediaType.TEXT_PLAIN).build();	
		}
		else
			return Response.ok(Constants.INVALIDUSER,MediaType.TEXT_PLAIN).build();
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	public String updateFile(@FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition headerOfFilePart,@FormDataParam("authors") String authors,@FormDataParam("comments") String comments,
            @FormDataParam("auth") String auth,@FormDataParam("trid") String updateDocId) {

		
		System.out.println(auth);
		
		WorkflowDao dao = new WorkflowDaoImpl();
		UserVo vo = dao.authenticateUser(auth.trim());

			if (vo != null) {
		
				
				
			// Check DB entry
					TrDocumentVo trVo = dao.getTrDetails(updateDocId);
			if( trVo!=null && trVo.getAuthList().contains(vo.getUserName()) &&
					!((trVo.getCurrentState().equalsIgnoreCase(Constants.PUBLISHED) ||  trVo.getCurrentState().equalsIgnoreCase(Constants.RFP) ||  trVo.getCurrentState().equalsIgnoreCase(Constants.RCP))))
				
			{
				if(trVo.getCurrentState().equalsIgnoreCase(Constants.RAP))
				{
					dao.setDocState(Integer.parseInt(updateDocId),1);
				}
				else if(trVo.getCurrentState().equalsIgnoreCase(Constants.ARP) && (headerOfFilePart.getFileName()!=null && headerOfFilePart.getFileName().length()!=0))
					dao.setDocState(Integer.parseInt(updateDocId),2);
				File file =null;
				if(trVo.getDescription().length()!=0 && headerOfFilePart.getFileName().length()!=0)
					file = new File("C:\\Users\\vsriganesh\\Desktop\\upload_files\\"+updateDocId+"."+trVo.getDescription());
				else
					file = new File("C:\\Users\\vsriganesh\\Desktop\\upload_files\\"+updateDocId);
				if(file!=null)
				{
				file.setWritable(true);
				file.delete();
				}
				
				
				
				
				
					// update name and format in DB
					
				if(headerOfFilePart.getFileName()!=null && headerOfFilePart.getFileName().length()!=0)		
				{
						dao.updateTrDescription(updateDocId,headerOfFilePart.getFileName());
						
						dao.updateTrCurrentCount(updateDocId,trVo.getReviewerCount());
						
						
				}
					
					
				// update modify date
				
				Date date = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy/M/dd");
				dao.updateTrModifyDate(updateDocId,df.format(date));
				
				
					// update authors
				
				
					if(authors!=null && authors!="" && authors.length()!=0)
					{
						
						
						
						String[] tempAuthor = authors.split(",");
						for(String temp : tempAuthor)
						{
							dao.docAuthUpdate(updateDocId, temp);
							
						}
						
						
					}
					
					
					
					
					if(headerOfFilePart.getFileName()!=null && headerOfFilePart.getFileName()!="" && headerOfFilePart.getFileName().length()!=0 )	
					{
					String format[] = headerOfFilePart.getFileName().split("\\.");
				
				
				String filePath;
				if (format.length == 2) {
					filePath = SERVER_UPLOAD_LOCATION_FOLDER + updateDocId + "."
							+ format[1];
				} else
					filePath = SERVER_UPLOAD_LOCATION_FOLDER + updateDocId;

				
				
				saveFile(fileInputStream, filePath);
				
				
				try {
					if(fileInputStream!=null)
					{
					fileInputStream.close();
					fileInputStream=null;
					System.gc();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					}
				
				return "<html><body> <u><center>File :<b>"
				+ headerOfFilePart.getFileName()
				+ " </b>updated successfully</center></u></b> <br/><center> Document ID : <b>"+updateDocId+"</b></center>  </body> </html>";
				
			}
				
			else
				return "Sorry!!! Current state of the document does not allow update to be performed";
		
			
			
	}
	
			else
				return Constants.INVALIDUSER;
			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * 
	 * Upload a File
	 */

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	public String uploadFile(@FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition headerOfFilePart,@FormDataParam("authors") String authors,
            @FormDataParam("auth") String auth,@FormDataParam("trid") String updateDocId) {

		
		
		
		WorkflowDao dao = new WorkflowDaoImpl();
		UserVo vo = dao.authenticateUser(auth.trim());

	if (vo != null) {
		
			// Make DB entry

			String trId = dao.newTrCreation(headerOfFilePart.getFileName(),vo,authors);

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

				
				
				saveFile(fileInputStream, filePath);
				try {
					if(fileInputStream!=null)
					{
					fileInputStream.close();
					fileInputStream=null;
					System.gc();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "<html><body> <u><center>File :<b>"
						+ headerOfFilePart.getFileName()
						+ " </b>uploaded successfully</center></u></b> <br/><center> Document ID : <b>"+trId+"</b></center>  </body> </html>";
			}

			else
				return "<html><body> <u><center>File :<b>"
						+ headerOfFilePart.getFileName()
						+ " </b>upload failed</center></u></b> </body> </html>";
		}
			return Constants.INVALIDUSER;
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
			uploadedInputStream.close();
			uploadedInputStream=null;
			outpuStream.close();
			outpuStream=null;
			System.gc();
					

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
			if(ret!=null && ((ret.getAuthList().contains(vo.getUserName()) || vo.getRole().equalsIgnoreCase(Constants.ADMIN)) && (!ret.getCurrentState().equalsIgnoreCase(Constants.PUBLISHED))))
			{
					
					if(dao.deleteTrDocument(trId)!=0)
					{
					File file = new File("C:\\Users\\vsriganesh\\Desktop\\upload_files\\"+trId+"."+ret.getDescription());
					file.setWritable(true);
					file.delete();
					return "File deleted !!!!! ";
					}
					else
						return "Sorry!!!! Unable to delete file";
						
			}
			
			else
				return Constants.INVALIDUSER;

			}
		else
			return Constants.INVALIDUSER;
		
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

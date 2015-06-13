package com.iiitb.test;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
@Path("/workflow1")
public class Test1WS {
	

	

		
		@GET
		@Produces(MediaType.TEXT_PLAIN)
		public String test()
		{
			return "Hi. My Second webservice test";
		}
		
		
		@PUT
		@Path("{rollNo}")
		@Produces(MediaType.TEXT_PLAIN)
		public String update(@PathParam("rollNo") String rollNo)
		{
			return "updated";
		}
		
}

package com.iiitb.test;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/workflow")
public class TestWS {


	  @GET
	  @Path("{roll}")
	  @Produces(MediaType.TEXT_PLAIN) public String test(@PathParam("roll") String test) { return
	  "Hi. My first webservice test "+test; }
	 



}

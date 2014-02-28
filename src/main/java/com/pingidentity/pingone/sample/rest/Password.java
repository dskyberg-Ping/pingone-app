package com.pingidentity.pingone.sample.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.logging.Logger;

@Path("rest/password/{email}")
public class Password {
	private static final Logger logger = Logger.getLogger(Password.class.getName());

    @GET
    @Produces("text/plain")
    public String getHello(@PathParam("email") String email) {
    	logger.info("Called Hello World");
        return String.format("<h1>Hello, %s!</h1>",email);
    }

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sayPlainTextHello() {
		String result = "Password Modified";
		return Response.status(201).entity(result).build();
	}

}

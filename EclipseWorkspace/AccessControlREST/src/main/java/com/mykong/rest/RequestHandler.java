package com.mykong.rest;
 
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
 
@Path("/hello")
public class RequestHandler {
 
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {
 
		String output = "Jersey say : " + msg;
 
		return Response.status(200).entity(output).build();
 
	}
	
	@POST
	@Path("/{request}")
	public Response postMsg(@PathParam("request") String msg) {
 
		String output = "Jersey request says : " + msg;
 
		return Response.status(200).entity(output).build();
 
	}
 
}
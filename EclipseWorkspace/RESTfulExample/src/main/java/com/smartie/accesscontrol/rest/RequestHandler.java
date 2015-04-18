package com.smartie.accesscontrol.rest;
 
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import smartie_dissertation.accesscontrol.main.PEP;
 
@Path("/hello")
public class RequestHandler {
 
	@GET
	public Response getMsg() {
		boolean result = PEP.ReturnTrue("");
		String output = "Jersey say : " + result + "  \n";
		return Response.status(200).entity(output).build();
	}
	
	@POST
	@FormParam("/{request}")
	public Response getMsg(@FormParam("request") String requestString) {
		String resultString = "";
		boolean result = PEP.EvaluateRequest(requestString, resultString);
		String output = "Jersey say : " + result;
		return Response.status(200).entity(output).build();
	}
 
}
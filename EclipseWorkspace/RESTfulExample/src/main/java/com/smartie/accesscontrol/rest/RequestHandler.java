package com.smartie.accesscontrol.rest;
 
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
//import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import attributedata_db.main.AttributeData;
import attributedata_db.main.AttributeDataDBManager;
import sensordata_db.main.SensorDataDBManager;
import smartie_dissertation.accesscontrol.main.PEP;
import smartie_dissertation.accesscontrol.main.SmartieResponse;
import smartie_dissertation.helperlib.generic.FileIOHandler;
//import smartie_dissertation.accesscontrol.main.PEP;
import smartie_dissertation.helperlib.generic.HelperFunctions;
import smartie_dissertation.helperlib.rest.RestResponse;
import smartie_dissertation.policy_db.main.PolicyDBManager;
 
@Path("/evaluateRequest")
public class RequestHandler { 	
	//TODO build rest interface for PAP - publishing and removing policies
	//TODO build a proper logger	
	//Temporary, used for storing requests and responses in log folder
	private static final String REQUEST_AND_RESPONSE_LOG_FOLDER = "C:\\Users\\Korisnik\\Google disk\\UA\\Dissertation\\ProjectWorkspace\\RESTfulExample\\log";
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject postTest() throws JSONException {
		RestResponse response = new RestResponse(true, "response in JSON or XML format");
		return response.toJSONObject();
		//return Response.status(200).entity("OK").build();
	}
	
	@POST
	@FormParam("/{request}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject postEvaluateRequest(@FormParam("request") String requestString) throws JSONException {
		SmartieResponse smartieResponse = PEP.EvaluateRequest(requestString);
		LogRequestAndResponse(requestString, smartieResponse);
		RestResponse response = new RestResponse(smartieResponse.getResult(), smartieResponse.getJSONResponse());
		return response.toJSONObject();
	}
	
	private void LogRequestAndResponse(String requestString, SmartieResponse smartieResponse){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		String name = "request_"+dateFormat.format(date);//HelperFunctions.StringToSH1(requestString);
		File requestFile = new File(RequestHandler.REQUEST_AND_RESPONSE_LOG_FOLDER+"\\"+name+".txt");
		FileIOHandler.outputToFile(requestFile, requestString);
		File responseFile = new File(RequestHandler.REQUEST_AND_RESPONSE_LOG_FOLDER+"\\"+name+"_response"+smartieResponse.getResult()+".txt");
		FileIOHandler.outputToFile(responseFile, smartieResponse.getResponse());
	}	
}
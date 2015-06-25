package smartie_dissertation.helperlib.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONObject;

import smartie_dissertation.helperlib.log.MyLog;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
 
/**
 * @author Vedran Semenski
 * 
 */
 
public class RestClient {
	
	protected Client client;
	protected WebResource service;
		
	public RestClient(String restBaseURI){		
		ClientConfig config = new DefaultClientConfig();
		this.client = Client.create(config);
		client.addFilter(new LoggingFilter());
		this.service = client.resource(restBaseURI);
	}

	//TODO add get,put,delete...
	
	public JSONObject postRequest(String functionName, HashMap<String, String> formData){
		MultivaluedMap<String, String> restFormData = new MultivaluedMapImpl();
//		JSONObject inputJsonObj = new JSONObject();
		for(Map.Entry<String, String> entry : formData.entrySet()){
			restFormData.add(entry.getKey(), entry.getValue());	
//			inputJsonObj.put(entry.getKey(), entry.getValue());	
		}
		JSONObject responseJsonObject = new JSONObject();
		try {		
			String response = this.service.path(functionName).accept(MediaType.APPLICATION_JSON).post(String.class, restFormData);
			MyLog.log("Response: "+response, MyLog.logMessageType.INFO, this.getClass().getName());
			responseJsonObject = new JSONObject(response);
//			responseJsonObject = this.service.path(functionName).accept(MediaType.APPLICATION_JSON).post(JSONObject.class, restFormData);
//			if (responseJsonObject.getStatus() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
//			} 				
		}catch (Exception e) {
			MyLog.log("Exception happened with a REST call", MyLog.logMessageType.ERROR, this.getClass().getName(), e);
		}		
		return responseJsonObject;	
	}
	
	public JSONObject postRequest(String functionName, JSONObject JsonData){
		JSONObject responseJsonObject = new JSONObject();
		try {		
			String response = this.service.path(functionName).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(String.class, JsonData.toString());
//			MyLog.log("Response: "+response, MyLog.logMessageType.INFO, this.getClass().getName());
			responseJsonObject = new JSONObject(response);
//			responseJsonObject = this.service.path(functionName).accept(MediaType.APPLICATION_JSON).post(JSONObject.class, restFormData);
//			if (responseJsonObject.getStatus() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
//			} 				
		}catch (Exception e) {
			MyLog.log("Exception happened with a REST call", MyLog.logMessageType.ERROR, this.getClass().getName(), e);
		}		
		return responseJsonObject;	
	}	
	
	public JSONObject getRequest(String functionName, HashMap<String, String> formData){
		MultivaluedMap<String, String> restFormData = new MultivaluedMapImpl();
//		JSONObject inputJsonObj = new JSONObject();
		int i = 0;
		for(Map.Entry<String, String> entry : formData.entrySet()){
			i++;
			functionName += (i==1) ? "?" : "&";
			restFormData.add(entry.getKey(), entry.getValue());	
			functionName += entry.getKey()+"="+entry.getValue();
		}
		JSONObject responseJsonObject = new JSONObject();
		try {		
			String response = this.service.path(functionName).accept(MediaType.APPLICATION_JSON).get(String.class);
			responseJsonObject = new JSONObject(response); 				
		}catch (Exception e) {
			MyLog.log("Exception happened with a REST call", MyLog.logMessageType.ERROR, this.getClass().getName(), e);
		}		
		return responseJsonObject;	
	}
	
}
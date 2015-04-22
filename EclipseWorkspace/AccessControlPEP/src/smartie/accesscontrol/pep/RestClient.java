package smartie.accesscontrol.pep;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONException;
import org.json.JSONObject;

import smartie_dissertation.helperlib.log.MyLog;
import smartie_dissertation.helperlib.rest.RestResponse;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
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
 
	private static final String REST_BASE_URI = "http://localhost:8080/RESTfulExample/";
	
	public static void main(String[] args) {
 
		RestClient client = new RestClient();
		client.evaluateRequest("");
	}
	
	private Client client;
	private WebResource service;
	
	public RestClient(){
		ClientConfig config = new DefaultClientConfig();
		this.client = Client.create(config);
		client.addFilter(new LoggingFilter());
		this.service = client.resource(REST_BASE_URI);
	}
 
	public RestResponse evaluateRequest(String request) {
		JSONObject responseJsonObject = new JSONObject();
		RestResponse response = new RestResponse();
		try {			
//			JSONObject inputJsonObj = new JSONObject();
//			inputJsonObj.put("request", request);			
			MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
			formData.add("request", request);			
			responseJsonObject = this.service.path("rest").path("evaluateRequest").accept(MediaType.APPLICATION_JSON).post(JSONObject.class, formData);
//			if (response.getStatus() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
//			} 		
			response = RestResponse.JsonToRestResponse(responseJsonObject);
			System.out.println(response.toString());			
		}catch (Exception e) {
			MyLog.log("Exception happened with a REST call", MyLog.logMessageType.ERROR, this.getClass().getName(), e);
		}		
		return response;
	}
}
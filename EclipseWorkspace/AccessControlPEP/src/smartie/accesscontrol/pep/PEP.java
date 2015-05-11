package smartie.accesscontrol.pep;

import java.util.HashMap;


import org.json.JSONException;
import org.json.JSONObject;

import smartie_dissertation.helperlib.log.MyLog;
import smartie_dissertation.helperlib.rest.RestClient;
import smartie_dissertation.helperlib.rest.RestResponse;


public class PEP {
	
	private static final String REST_BASE_URI = "http://localhost:8080/RESTfulExample/";
	private static final String REST_EVALUATE_REQUEST_FUNCTION = "rest/evaluateRequest";	
	
	public PEP(){
		//TODO setup configuration
	}
	
	/**
	 * @param request  - XACML request in JSON of XML form
	 * @return - The boolean return value relates to the response of a XACML service. True if the request is allowed to execute and False in the case it is not allowed.
	 */
	public boolean ExecuteRequest(String request){
		RestClient client = new RestClient(REST_BASE_URI);
		HashMap<String, String> formData = new HashMap<>();
		formData.put("request", request);
		JSONObject jsonResponse = client.postRequest(REST_EVALUATE_REQUEST_FUNCTION, formData);
		RestResponse response;
		boolean result = false;
		try {
			response = RestResponse.JsonToRestResponse(jsonResponse);
			result = response.result;
		} catch (JSONException e) {
			MyLog.log("Rest service resposnse JSONObject doesn't have required fields", MyLog.logMessageType.ERROR, PEP.class.getName(), e);
			result = false;
		}
		return result;		
	}
	
	/**
	 * @param request - XACML request in JSON of XML form
	 * @param resourceFetcher - object contains execute and terminate "triggers" that are called in the case of 
	 * 							responses allowing or denying execution. 
	 * 							A positive response results in the calling of a execute command and a negative in the calling of a terminate command 
	 * @return - The boolean return value relates to the response of a XACML service. True if the request is allowed to execute and False in the case it is not allowed.
	 */
	public boolean ExecuteRequest(String request, IResourceFetcher resourceFetcher){
		boolean result = this.ExecuteRequest(request);
		if(result){
			resourceFetcher.execute();
		}else{
			resourceFetcher.terminate();
		}
		return result;
	}	
	
}

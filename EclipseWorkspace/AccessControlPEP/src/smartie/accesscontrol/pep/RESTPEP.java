package smartie.accesscontrol.pep;

import java.util.HashMap;





import org.json.JSONException;
import org.json.JSONObject;

import smartie_dissertation.helperlib.XACML.BasicPEP;
//import smartie_dissertation.helperlib.XACML.IResourceFetcher;
import smartie_dissertation.helperlib.log.MyLog;
import smartie_dissertation.helperlib.rest.RestClient;
import smartie_dissertation.helperlib.rest.RestResponse;
//import smartie_dissertation.helperlib.rest.SecureRestClient;


/**
 * @author Vedran Semenski
 * class is used for enforcing AccessControl by connecting to a REST service
 */
public class RESTPEP extends BasicPEP {
	
	private static final String DEFAULT_REST_BASE_URI = "http://localhost:8080/RESTfulExample/";
	private static final String REST_EVALUATE_REQUEST_FUNCTION = "rest/evaluateRequest";	
	
	private static String REST_BASE_URI = DEFAULT_REST_BASE_URI;
	
	/**
	 * keeps the default configuration
	 */
	public RESTPEP(){
	}	
	
	/**
	 * Initializes the class by configuring it with the baseURI
	 * @param baseURI - base URI of the target REST service
	 */
	public RESTPEP(String baseURI){
		REST_BASE_URI = baseURI;
	}
	
	/**
	 * @param request  - XACML request in JSON of XML form
	 * @return - The boolean return value relates to the response of a XACML service. True if the request is allowed to execute and False in the case it is not allowed.
	 */
	@Override
	public boolean ExecuteRequest(String request){
		RestClient client = new RestClient(REST_BASE_URI);		
//		SecureRestClient client = new 		
		HashMap<String, String> formData = new HashMap<>();
		formData.put("request", request);
		JSONObject jsonResponse = client.postRequest(REST_EVALUATE_REQUEST_FUNCTION, formData);
		RestResponse response;
		boolean result = false;
		try {
			response = RestResponse.JsonToRestResponse(jsonResponse);
			result = response.result;
		} catch (JSONException e) {
			MyLog.log("Rest service response JSONObject doesn't have required fields", MyLog.logMessageType.ERROR, RESTPEP.class.getName(), e);
			result = false;
		}
		return result;		
	}	
}

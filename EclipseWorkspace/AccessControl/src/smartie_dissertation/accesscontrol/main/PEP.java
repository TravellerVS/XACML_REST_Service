package smartie_dissertation.accesscontrol.main;

import com.att.research.xacml.api.Response;

public class PEP {	
	public static boolean EvaluateRequest(String requestString){
		ContextHandler ch = new ContextHandler();
		Response response = null;
		//TODO Built call to ContextHandler/Request evaluation as a REST call		
		boolean result = ch.EvaluateRequest(requestString, response);		
		return result;
	}
	
	public static boolean EvaluateRequest(String requestString, String responseString){
		Response response = null;
		//TODO Built call to ContextHandler/Request evaluation as a REST call
		ContextHandler ch = new ContextHandler();
		boolean result = ch.EvaluateRequest(requestString, response);	
		responseString = (response != null) ? response.toString() : "";
		return result;
	}
	
	public static boolean ReturnTrue(String requestString){
		return true;
	}
}

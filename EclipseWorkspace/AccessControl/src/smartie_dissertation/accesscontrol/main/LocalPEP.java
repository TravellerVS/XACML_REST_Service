package smartie_dissertation.accesscontrol.main;

import smartie_dissertation.helperlib.XACML.BasicPEP;

public class LocalPEP extends BasicPEP{	
	
	public LocalPEP(){
		//setup configuration
	}
	
	public static SmartieResponse EvaluateRequest(String requestString){
		ContextHandler ch = new ContextHandler();
		SmartieResponse response = ch.EvaluateRequest(requestString);
		ch.close();
		return response;
	}	
	
	/**
	 * @param request  - XACML request in JSON of XML form
	 * @return - The boolean return value relates to the response of a XACML service. True if the request is allowed to execute and False in the case it is not allowed.
	 */
	public boolean ExecuteRequest(String request){
		ContextHandler ch = new ContextHandler();
		SmartieResponse response = ch.EvaluateRequest(request);
		ch.close();
		return response.getResult();		
	}	
	
//	public static boolean ReturnTrue(String requestString){
//		return true;
//	}
	
}

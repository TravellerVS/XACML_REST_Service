package smartie.accesscontrol.pep;

import org.json.JSONObject;

import smartie_dissertation.helperlib.rest.RestResponse;

public class PEP {
	
	//TODO make request functions with a delegate
	//TODO add success delegate and add fail delegate to be executed
	public static boolean ExecuteRequest(String request){
		RestClient client = new RestClient();
		RestResponse response = client.evaluateRequest(request);
		if(response.result == true){
			//TODO make delegate execute
		}else{
			//TODO make delegate fail
		}
		return response.result;		
	}		
}

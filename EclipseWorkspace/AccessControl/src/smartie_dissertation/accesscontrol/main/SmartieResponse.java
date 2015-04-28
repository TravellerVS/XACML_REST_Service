package smartie_dissertation.accesscontrol.main;

import com.att.research.xacml.api.Response;
import com.att.research.xacml.std.dom.DOMResponse;
import com.att.research.xacml.std.json.JSONResponse;

/**
 * @author Vedran Semenski
 *
 *this is a simple class for holding the response and result
 */
public class SmartieResponse {
	private boolean result;
	private String responseString;
	private Response response;
	
	public SmartieResponse(boolean result, Response response){
		this.result = result;
		this.response = response;
		if(response != null){
			this.responseString = getJSONResponse();
			if(this.responseString.isEmpty()){
				this.responseString = getXMLResponse();
			}
		}
	}
	
	@Override
	public String toString(){
		return getResponse();
	}
	
	public String getResponse(){
		return this.responseString;
	}
	
	public String getJSONResponse(){
		String response = "";
		try {
			response = JSONResponse.toString(this.response, true);
		} catch (Exception e) {	
			response  = "";
		}
		return response;
	}
	public String getXMLResponse(){
		String response = "";
		try {
			response = DOMResponse.toString(this.response, true);
		} catch (Exception e) {	
			response  = "";
		}
		return response;
	}
	
	public boolean getResult(){
		return this.result;
	}
}

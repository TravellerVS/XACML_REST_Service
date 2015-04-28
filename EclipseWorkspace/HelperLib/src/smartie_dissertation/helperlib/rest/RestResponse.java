package smartie_dissertation.helperlib.rest;

import org.json.JSONException;
import org.json.JSONObject;

public class RestResponse {
	
	public boolean result;
	public String response;
	
	public RestResponse(){
		this.result = false;
		this.response = "";
	}
	
	public RestResponse(boolean result, String response){
		this.result = result;
		this.response = response;
	}
	
	@Override
    public String toString() {
        return new StringBuffer(" result : ").append(this.result)
            .append(" response : ").append(this.response).toString();
    }
	
	public JSONObject toJSONObject() throws JSONException{
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("result", this.result);
        outputJsonObj.put("response", this.response);
        return outputJsonObj;
	}
	
	public static RestResponse JsonToRestResponse(JSONObject jsonObject) throws JSONException{
		return new RestResponse(jsonObject.getBoolean("result"), jsonObject.getString("response"));	
	}
}

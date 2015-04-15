package smartie_dissertation.accesscontrol.main;

import java.io.File;

import smartie_dissertation.helperlib.generic.FileIOHandler;
import smartie_dissertation.helperlib.generic.HelperFunctions;
import smartie_dissertation.helperlib.log.MyLog;

import com.att.research.xacml.api.Request;
import com.att.research.xacml.std.dom.DOMRequest;
import com.att.research.xacml.std.json.JSONRequest;

public class RequestParser {
	
	private static enum RequestType {
	    JSON, XML
	}
		
	public static Request generateRequest(File requestFile) {
		String requestString = FileIOHandler.readFromFile(requestFile);
		return generateRequest(requestString);
	}
	
	public static Request generateRequest(String requestString) {
		Request request = null;
		//TODO validate in a better way that the request is XML or JSON
		RequestType type = (HelperFunctions.isJSONValid(requestString)) ? RequestParser.RequestType.JSON : RequestParser.RequestType.XML;
		request = generateRequest(requestString, type);
		return request;
	}
	
	private static Request generateRequest(String requestString, RequestParser.RequestType type) {
		//
		// Convert to a XACML Request Object
		//
		Request request = null;
		try{
			if (type == RequestParser.RequestType.JSON) {
				request = JSONRequest.load(requestString);
			} else if (type == RequestParser.RequestType.XML) {
				request = DOMRequest.load(requestString);
			}
			if (request == null) {
				MyLog.log("Invalid request type. ", MyLog.logMessageType.ERROR, RequestParser.class.toString());
			}
		}	
		catch(Exception e){		
			MyLog.log("Request could not be parsed: "+ e.getMessage(), MyLog.logMessageType.ERROR, RequestParser.class.toString());
		}
		return request;
	}
	
//TODO make xml->json and json->xml conversion functions
	
/*
 * Deprecated methods
 */
//	public static String convertToJSON(String request){
//	String result = "";
//	return testRequestJSON;
//}
//
//public static String convertToXML(String request){
//	String result = "";
//	return testRequestXML;
//}	
	
}

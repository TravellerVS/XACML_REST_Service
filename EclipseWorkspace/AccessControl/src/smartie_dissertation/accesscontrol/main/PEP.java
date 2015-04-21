package smartie_dissertation.accesscontrol.main;

import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;

import smartie_dissertation.helperlib.generic.HelperFunctions;

import com.att.research.xacml.api.Response;
import com.att.research.xacml.std.StdResponse;
import com.att.research.xacml.std.dom.DOMResponse;
import com.att.research.xacml.std.json.JSONResponse;

public class PEP {	
	
	public static SmartieResponse EvaluateRequest(String requestString){
		ContextHandler ch = new ContextHandler();
		return ch.EvaluateRequest(requestString);
	}	
	public static boolean ReturnTrue(String requestString){
		return true;
	}
}

package smartie_dissertation.accesscontrol.main;

import java.util.Collection;

import smartie_dissertation.helperlib.log.MyLog;

import com.att.research.xacml.api.Decision;
import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.Response;
import com.att.research.xacml.api.Result;
import com.att.research.xacml.api.pdp.PDPEngine;
import com.att.research.xacml.api.pdp.PDPEngineFactory;
import com.att.research.xacml.api.pdp.PDPException;
import com.att.research.xacml.std.json.JSONResponse;
import com.att.research.xacml.util.FactoryException;
//import com.att.research.xacmlatt.pdp.test.*;

public class PDP{
	
	private PDPEngine engine = null;

	public PDP(){
		configure();
	}
	
	protected void configure(){		
		PDPEngineFactory factory;
		try {
			factory = PDPEngineFactory.newInstance();
			this.engine  = factory.newEngine();
		} catch (FactoryException e) {
			MyLog.log(e.getMessage(), MyLog.logMessageType.ERROR, this.getClass().toString());
		}		
	}
	
	public SmartieResponse EvaluateRequest(Request request){
//		response = null;
		Response response = null;
		boolean result = false;
		try {
			response = this.engine.decide(request);
			Collection<Result> results = response.getResults();
			for (Result r : results) {
				if (r.getDecision().equals(Decision.PERMIT)) {
					result = true;
					break;
				}
			}
		} catch (PDPException e) {
			MyLog.log(e.getMessage(), MyLog.logMessageType.DEBUG, this.getClass().toString());
		}
		SmartieResponse smartieResponse = new SmartieResponse(result, response);
		return smartieResponse;		
	}
	
//	public static void test(String[] args) {
//		TestBase.main(args);
//	}
}
package smartie_dissertation.accesscontrol.main;

public class PEP {	
	
	public static SmartieResponse EvaluateRequest(String requestString){
		ContextHandler ch = new ContextHandler();
		SmartieResponse response = ch.EvaluateRequest(requestString);
		ch.close();
		return response;
	}	
//	public static boolean ReturnTrue(String requestString){
//		return true;
//	}
}

package smartie_dissertation.accesscontrol.main;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacmlatt.pdp.policy.PolicyDef;
import com.att.research.xacmlatt.pdp.policy.dom.DOMPolicyDef;

import smartie_dissertation.helperlib.log.MyLog;
import smartie_dissertation.policy_db.main.PolicyDBManager;

public class PRP {
	/**
	 * @return list of all policies stored in the policy database
	 */
	public static Map<String,String> getPolicies(){
		Map<String,String> policyList = new HashMap<>();
		PolicyDBManager pm = PolicyDBManager.getInstance();
		policyList = pm.getPolicies();		
		return policyList;
	}
	
	public static Map<String,String> getPolicies(String request){
		//TODO: fetch smaller set of policies depending on the request
		return getPolicies();
	}
	
	public static List<PolicyDef> getPolicyDefs() {		
		Map<String,String> policyList = getPolicies();
		List<PolicyDef> listPolicyDefs	= new ArrayList<PolicyDef>();
		for(String policyString : policyList.values())
		{
			InputStream inputStream = null;
			try {
				inputStream = new ByteArrayInputStream(policyString.getBytes());
				PolicyDef policyDef	= DOMPolicyDef.load(inputStream);
				listPolicyDefs.add(policyDef);				
			} catch (DOMStructureException e) {
				MyLog.log(e.getMessage(), MyLog.logMessageType.ERROR, PRP.class.toString(), e);
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						MyLog.log("Exception closing InputStream for policy.", MyLog.logMessageType.ERROR, PRP.class.toString());
					}
				}
			}
		}
		return listPolicyDefs;
	}	
}

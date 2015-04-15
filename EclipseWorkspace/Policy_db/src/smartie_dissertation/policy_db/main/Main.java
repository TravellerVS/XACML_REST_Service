package smartie_dissertation.policy_db.main;

import java.io.File;

import smartie_dissertation.helperlib.generic.FileIOHandler;
import smartie_dissertation.helperlib.log.MyLog;

public class Main {

	public static void main(String[] args) {
		PolicyDBManager pm = PolicyDBManager.getInstance();
		String basePath = "C:\\Users\\Korisnik\\Google disk\\UA\\Dissertation\\ProjectWorkspace\\Policy_db\\test_policies\\";
		File file = new File(basePath+"demo-11.xml");		
		String policy = FileIOHandler.readFromFile(file);
		String policyID = pm.storePolicy(policy);
		policyID = pm.storePolicy(policy);
		pm.removePolicy(policyID);
		policyID = pm.storePolicy(policy);
		pm.removePolicy(policyID);
		String p = pm.getPolicy(policyID);
		pm.removePolicy(policyID);
		MyLog.log(p, MyLog.logMessageType.DEBUG, Main.class.getName());
		p = null;
		pm.outputKeys();
		pm.close();
		MyLog.log("FINISHED", MyLog.logMessageType.INFO, Main.class.getName());		
	}
}

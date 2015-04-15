package smartie_dissertation.accesscontrol.main;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import smartie_dissertation.helperlib.generic.FileIOHandler;
import smartie_dissertation.helperlib.log.MyLog;
import smartie_dissertation.policy_db.main.PolicyDBManager;

//TODO Build real PAP
public class PAP {	
	public static void loadPolicies(String folderPath){	
		PolicyDBManager pm = PolicyDBManager.getInstance();
		File directory = new File(folderPath);
		String[] extensions = new String[] { "xml" };
		List<File> files = (List<File>) FileUtils.listFiles(directory , extensions, true);		
	    for (File policyFile : files) {
	    	if (policyFile.isFile() ) {
	    		String policy = FileIOHandler.readFromFile(policyFile);
	    		pm.storePolicy(policy);
	    		MyLog.log("Policy file \"" + policyFile.getName() + "\" loaded successfully.", MyLog.logMessageType.DEBUG, PAP.class.toString());
	    	} 
	    }
	}
//	public static void loadPolicies(){
//		loadPolicies(DEFAULT_POLICY_FOLDER_PATH);
//	}
}

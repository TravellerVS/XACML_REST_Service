package smartie_dissertation.accesscontrol.main;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import smartie_dissertation.helperlib.generic.FileIOHandler;
import smartie_dissertation.helperlib.log.MyLog;
import smartie_dissertation.policy_db.main.PolicyDBManager;

//TODO implement the fetching of data/resources
public class Main {
	
	private static final String REQUEST_TEST_DIR_PATH = "C:\\Users\\Korisnik\\Google disk\\UA\\Dissertation\\ProjectWorkspace\\AccessControl\\TestFolder\\requests";
	private static final String POLICY_TEST_DIR_PATH = "C:\\Users\\Korisnik\\Google disk\\UA\\Dissertation\\ProjectWorkspace\\AccessControl\\TestFolder\\policies";
	
	public static void main(String[] args) {
		test();
	}
	
	public static void test() {	
		/*
		 * Initialize Policy Manager and load all the policies in the database for testing
		 */
		PolicyDBManager pm = PolicyDBManager.getInstance();
		pm.deleteAllPolicies();
		PAP.loadPolicies(POLICY_TEST_DIR_PATH);
		
		/*
		 * Go trough the test requests and run evaluate them against the policies
		 */
		File directory = new File(REQUEST_TEST_DIR_PATH);
		String[] extensions = new String[] { "xml", "json" };
		List<File> files = (List<File>) FileUtils.listFiles(directory, extensions, true);
		String resultMessage = "\n";
	    for (File requestFile : files) {
	    	if (requestFile.isFile() ) {
	    		String request = FileIOHandler.readFromFile(requestFile);
	    		//TODO Convert static method call to REST
	    		boolean result = PEP.EvaluateRequest(request);
	    		String message = "Request file \"" + requestFile.getName() + "\" returned: " + result;
	    		resultMessage += message+"\n";
	    		MyLog.log(message, MyLog.logMessageType.DEBUG, Main.class.toString());
	    	} 
	    }
	    MyLog.log(resultMessage, MyLog.logMessageType.INFO, Main.class.toString());
	}
}

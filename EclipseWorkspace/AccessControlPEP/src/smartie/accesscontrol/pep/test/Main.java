package smartie.accesscontrol.pep.test;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;

import smartie.accesscontrol.pep.*;
import smartie_dissertation.helperlib.XACML.IResourceFetcher;
import smartie_dissertation.helperlib.generic.FileIOHandler;
import smartie_dissertation.helperlib.log.MyLog;

public class Main {
	
	private static final String REQUEST_TEST_DIR_PATH = "C:\\Users\\Korisnik\\Google disk\\UA\\Dissertation\\ProjectWorkspace\\AccessControlPEP\\TestFolder\\requests";

	public static void main(String[] args) {
		
		IResourceFetcher rf = new ResourceFetcher();
		RESTPEP pep = new RESTPEP();		
		/*
		 * Go trough the test requests and run evaluate them against the policies
		 */
		File directory = new File(REQUEST_TEST_DIR_PATH);
		String[] extensions = new String[] { "xml", "json" };
		List<File> files = (List<File>) FileUtils.listFiles(directory, extensions, true);
		String resultMessage = "\n";
		long millisStart = Calendar.getInstance().getTimeInMillis();
	    for (File requestFile : files) {
	    	if (requestFile.isFile() ) {
	    		String request = FileIOHandler.readFromFile(requestFile);
	    		millisStart = Calendar.getInstance().getTimeInMillis();
	    		boolean response1 = pep.ExecuteRequest(request, rf);
	    		long millisEnd = Calendar.getInstance().getTimeInMillis();
	    		boolean response2 = pep.ExecuteRequest(request);
	    		String message = "Request file \"" + requestFile.getName() + "\" returned: " + response1 + ", "+ response2;
	    		resultMessage += message+ " time: "+ (millisEnd-millisStart) +"ms \n";	
	    		MyLog.log(message , MyLog.logMessageType.DEBUG, Main.class.toString());
	    	} 
	    }
	    MyLog.log(resultMessage, MyLog.logMessageType.INFO, Main.class.toString());
	    MyLog.log("FINISHED", MyLog.logMessageType.INFO, Main.class.toString());   
	}
}

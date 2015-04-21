package smartie_dissertation.accesscontrol.main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import smartie_dissertation.helperlib.generic.FileIOHandler;
import smartie_dissertation.helperlib.log.MyLog;

import com.att.research.xacml.api.Request;
import com.att.research.xacml.api.Response;
import com.att.research.xacml.util.XACMLProperties;

public class ContextHandler {
	
	private static final String DEFAULT_PROPERTIES_FOLDER_PATH = "C:\\Users\\Korisnik\\Google disk\\UA\\Dissertation\\ProjectWorkspace\\AccessControl\\TestFolder";
		
	public ContextHandler()	{
		configure(DEFAULT_PROPERTIES_FOLDER_PATH);
	}
		
	public SmartieResponse EvaluateRequest(File requestFile)	{
		String requestString = FileIOHandler.readFromFile(requestFile);
		return EvaluateRequest(requestString);
	}
	
	public SmartieResponse EvaluateRequest(String requestString)	{
		//TODO retrieve subject, object and environment attributes
		Request request = RequestParser.generateRequest(requestString);
		PDP pdp = new PDP();
		return pdp.EvaluateRequest(request);
	}
	
	protected void configure(String propertiesFilePath){
		RunConfigurations();
	}
	
	private static void RunConfigurations()
	{
		RunConfigurations(DEFAULT_PROPERTIES_FOLDER_PATH);
	}
	
	private static void RunConfigurations(String propertiesFilePath)
	{
		//
		// Setup the xacml.properties file
		//
		if (propertiesFilePath == null) {
			throw new IllegalArgumentException("Must supply a path to a test directory.");
		}
		Path pathDir = Paths.get(propertiesFilePath, "xacml.properties");
		if (Files.notExists(pathDir)) {
			throw new IllegalArgumentException(pathDir.toString() + " does not exist.");
		}
		//
		// Set it as the System variable so the XACML factories know where the properties are
		// loaded from.
		//
		System.setProperty(XACMLProperties.XACML_PROPERTIES_NAME, pathDir.toString());
	}
}

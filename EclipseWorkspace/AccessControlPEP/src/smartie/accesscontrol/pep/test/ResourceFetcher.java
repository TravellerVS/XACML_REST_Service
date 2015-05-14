package smartie.accesscontrol.pep.test;

import smartie.accesscontrol.pep.*;
import smartie_dissertation.helperlib.XACML.IResourceFetcher;
import smartie_dissertation.helperlib.log.MyLog;

public class ResourceFetcher implements IResourceFetcher{

	@Override
	public void execute() {
		MyLog.log("EXECUTING the fetching of resources.", MyLog.logMessageType.INFO, this.getClass().getName());
	}

	@Override
	public void terminate() {
		MyLog.log("TERMINATING th fetching of resources.", MyLog.logMessageType.INFO, this.getClass().getName());		
	}

}

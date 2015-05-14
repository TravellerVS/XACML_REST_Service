package smartie_dissertation.helperlib.XACML;


public abstract class BasicPEP {
	
	public BasicPEP(){
		//TODO setup configuration
	}
	
	/**
	 * @param request  - XACML request in JSON of XML form
	 * @return - The boolean return value relates to the response of a XACML service. True if the request is allowed to execute and False in the case it is not allowed.
	 */
	public abstract boolean ExecuteRequest(String request);
	
	/**
	 * @param request - XACML request in JSON of XML form
	 * @param resourceFetcher - object contains execute and terminate "triggers" that are called in the case of 
	 * 							responses allowing or denying execution. 
	 * 							A positive response results in the calling of a execute command and a negative in the calling of a terminate command 
	 * @return - The boolean return value relates to the response of a XACML service. True if the request is allowed to execute and False in the case it is not allowed.
	 */
	public boolean ExecuteRequest(String request, IResourceFetcher resourceFetcher){
		boolean result = this.ExecuteRequest(request);
		if(result){
			resourceFetcher.execute();
		}else{
			resourceFetcher.terminate();
		}
		return result;
	}	
}

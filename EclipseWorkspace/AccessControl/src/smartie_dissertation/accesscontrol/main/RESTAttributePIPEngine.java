package smartie_dissertation.accesscontrol.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;




import org.json.JSONException;
import org.json.JSONObject;

//import smartie.accesscontrol.pep.RESTPEP;
import smartie_dissertation.helperlib.XACML.AttributeFactory;
import smartie_dissertation.helperlib.XACML.SmartieXACML;
import smartie_dissertation.helperlib.log.MyLog;
import smartie_dissertation.helperlib.rest.RestClient;
import smartie_dissertation.helperlib.rest.RestResponse;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.api.pip.PIPResponse;
import com.att.research.xacml.util.FactoryException;

public class RESTAttributePIPEngine extends SmartiePIPEngine {
	//TODO make rest fetching service
	public RESTAttributePIPEngine() {
		
	}
	
	protected static Set<PIPRequest> AttributeListToRequestSet(List<Attribute> attributeList){
		Set<PIPRequest> returnSet = new HashSet<PIPRequest>();
//		for(Attribute attribute : attributeList){
//			returnSet.add(new StdPIPRequest(attribute));					
//		}
		return returnSet;
	}
	
	@Override
	protected void loadAttributesRequired() {		
		this.listAttributesRequired	= new ArrayList<Attribute>();
		AttributeFactory attributeFactory = AttributeFactory.getInstance();
		try {
			this.listAttributesRequired.add(attributeFactory.createAttribute(SmartieXACML.URN_SUBJECT_CATEGORY_ACCESS_SUBJECT, SmartieXACML.URN_ATTRIBUTE_ID_SUBJECT_PREFIX+"api-key", SmartieXACML.DATATYPE_STRING));
			this.listAttributesRequired.add(attributeFactory.createAttribute(SmartieXACML.URN_CATEGORY_RESOURCE, SmartieXACML.URN_ATTRIBUTE_ID_RESOURCE_PREFIX+"resource-path", SmartieXACML.DATATYPE_STRING));
		} catch (FactoryException e){
			MyLog.log("Error loading provided attributes", MyLog.logMessageType.ERROR, this.getName(), e);
		}
	}	
	@Override
	protected void loadAttributesProvided(){
		this.listAttributesProvided	= new ArrayList<Attribute>();	
		
		AttributeFactory attributeFactory = AttributeFactory.getInstance();
		try {
			this.listAttributesProvided.add(attributeFactory.createAttribute(SmartieXACML.URN_SUBJECT_CATEGORY_ACCESS_SUBJECT, SmartieXACML.URN_ATTRIBUTE_ID_SUBJECT_PREFIX+"subject-id", SmartieXACML.DATATYPE_STRING));
			this.listAttributesProvided.add(attributeFactory.createAttribute(SmartieXACML.URN_CATEGORY_RESOURCE, SmartieXACML.URN_ATTRIBUTE_ID_RESOURCE_PREFIX+"owner-id", SmartieXACML.DATATYPE_STRING));
			this.listAttributesProvided.add(attributeFactory.createAttribute(SmartieXACML.URN_CATEGORY_RESOURCE, SmartieXACML.URN_ATTRIBUTE_ID_RESOURCE_PREFIX+"action-name", SmartieXACML.DATATYPE_STRING));
		} catch (FactoryException e){
			MyLog.log("Error loading provided attributes", MyLog.logMessageType.ERROR, this.getName(), e);
		}
				
//		AttributeFactory attributeFactory = AttributeFactory.getInstance();
//		try {
//			this.listAttributesProvided.add(attributeFactory.createAttribute(SmartieXACML.URN_CATEGORY_ENVIRONMENT, ENVIRONMENT_ATTRIBUTE_CURRENT_DATE, SmartieXACML.DATATYPE_DATE));
//			this.listAttributesProvided.add(attributeFactory.createAttribute(SmartieXACML.URN_CATEGORY_ENVIRONMENT, ENVIRONMENT_ATTRIBUTE_CURRENT_TIME, SmartieXACML.DATATYPE_TIME));
//			this.listAttributesProvided.add(attributeFactory.createAttribute(SmartieXACML.URN_CATEGORY_ENVIRONMENT, ENVIRONMENT_ATTRIBUTE_CURRENT_DATETIME, SmartieXACML.DATATYPE_DATETIME));
//		} catch (FactoryException e) {
//			MyLog.log("Error loading provided attributes", MyLog.logMessageType.ERROR, this.getName(), e);
//		}
	}
	
//	private void loadAttributes() {		
////		AttributeDataDBManager dbManager = DBManagerFactory.getDataManager(AttributeDataDBManager.class);
////		this.listAttributesLoaded = dbManager.fetchAllEnvironmentAttributes();
//		
//		//TODO: fetch the attribute values and make attributes from that
//		
//		
//		//RestClient client = new RestClient(REST_BASE_URI);
//		RestClient client = new RestClient("asdasd");
////		SecureRestClient client = new 		
//		HashMap<String, String> formData = new HashMap<>();
//		formData.put("request", "data");
//		
//		JSONObject jsonResponse = client.postRequest(REST_EVALUATE_REQUEST_FUNCTION, formData);
//		RestResponse response;
//		boolean result = false;
//		try {
//			response = RestResponse.JsonToRestResponse(jsonResponse);
//			result = response.result;
//		} catch (JSONException e) {
//			MyLog.log("Rest service resposnse JSONObject doesn't have required fields", MyLog.logMessageType.ERROR, RESTPEP.class.getName(), e);
//			result = false;
//		}
//	}

	@Override
	public PIPResponse getAttributes(PIPRequest pipRequest, PIPFinder pipFinder) throws PIPException {
		if(this.listAttributesLoaded.isEmpty()){
			RestClient client = new RestClient("REST_BASE_URI");
//			SecureRestClient client = new 		
			HashMap<String, String> formData = new HashMap<>();
			formData.put("request", "request");
			
			JSONObject jsonResponse = client.postRequest("REST_EVALUATE_REQUEST_FUNCTION", formData);
			RestResponse response;
			boolean result = false;
			try {
				response = RestResponse.JsonToRestResponse(jsonResponse);
				result = response.result;
			} catch (JSONException e) {
				MyLog.log("Rest service resposnse JSONObject doesn't have required fields", MyLog.logMessageType.ERROR, this.getName(), e);
				result = false;
			}
		}
		return getAttributesFromLoaddedList(pipRequest, pipFinder);
	}

	@Override
	public void configure(String id, Properties properties) throws PIPException {
		this.name	= id;
		this.description	= properties.getProperty(id + PROP_DESCRIPTION);
		if (this.description == null) {
			this.description	= "PIPEngine for loading attributes via external REST service.";
		}
//		this.loadAttributes();
		this.loadAttributesProvided();
		this.loadAttributesRequired();
	}

}

package smartie_dissertation.accesscontrol.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
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
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.api.pip.PIPResponse;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.util.FactoryException;

public class RESTAttributePIPEngine extends SmartiePIPEngine {
		
	private static final String SUBJECT_ATTRIBUTE_API_KEY 	= SmartieXACML.URN_ATTRIBUTE_ID_SUBJECT_PREFIX+"api-key";
	private static final String RESOURCE_ATTRIBUTE_RESOURCE_PATH 	= SmartieXACML.URN_ATTRIBUTE_ID_RESOURCE_PREFIX+"resource-path";
	
	private static final String REST_BASE_URI 	= "http://193.136.93.93:5555/api/";
	private static final String REST_FUNCTION_NAME 	= "xacml";
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
			this.listAttributesRequired.add(attributeFactory.createAttribute(SmartieXACML.URN_SUBJECT_CATEGORY_ACCESS_SUBJECT, SUBJECT_ATTRIBUTE_API_KEY, SmartieXACML.DATATYPE_STRING));
			this.listAttributesRequired.add(attributeFactory.createAttribute(SmartieXACML.URN_CATEGORY_RESOURCE, RESOURCE_ATTRIBUTE_RESOURCE_PATH, SmartieXACML.DATATYPE_STRING));
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
			this.listAttributesProvided.add(attributeFactory.createAttribute(SmartieXACML.URN_SUBJECT_CATEGORY_ACCESS_SUBJECT, SmartieXACML.URN_ATTRIBUTE_ID_SUBJECT_PREFIX+"permission-read", SmartieXACML.DATATYPE_BOOLEAN));
			this.listAttributesProvided.add(attributeFactory.createAttribute(SmartieXACML.URN_SUBJECT_CATEGORY_ACCESS_SUBJECT, SmartieXACML.URN_ATTRIBUTE_ID_SUBJECT_PREFIX+"permission-write", SmartieXACML.DATATYPE_BOOLEAN));
		} catch (FactoryException e){
			MyLog.log("Error loading provided attributes", MyLog.logMessageType.ERROR, this.getName(), e);
		}				
	}
	
	private void loadAttributes(PIPRequest pipRequest, PIPFinder pipFinder) {		
		this.listAttributesLoaded = new ArrayList<Attribute>();			
		try {
			HashMap<PIPRequest, AttributeValue<?>> requiredAttributeValues = getRequiredAttributeValues(pipRequest, pipFinder);
			HashMap<String, String> formData = new HashMap<>();
			JSONObject requestParams = new JSONObject();
			for(Entry<PIPRequest, AttributeValue<?>> attributeValue : requiredAttributeValues.entrySet()){
				String attributeID = attributeValue.getKey().getAttributeId().stringValue();
				String value = DataTypes.DT_STRING.convert(attributeValue.getValue().getValue());
				switch(attributeID){
					case SUBJECT_ATTRIBUTE_API_KEY:
						formData.put("api_key", value);
						requestParams.put("api_key", value);
						break;
					case RESOURCE_ATTRIBUTE_RESOURCE_PATH:
						formData.put("topic", value);
						requestParams.put("topic", value);
						break;
					default :
						break;
				}
			}
			
			if(formData.size()!=this.listAttributesRequired.size()){
				throw new PIPException("Not all of the required attributes were provided");
			}			
			RestClient client = new RestClient(REST_BASE_URI);	
//			String requestParams = "{\"api_key\": \"AgezH7a4Whv73bmnSMaNpes6fbPgdmDW\", \"topic\": \"a\"}";
			JSONObject jsonResponse = client.postRequest(REST_FUNCTION_NAME, requestParams);			
			//TODO refactor
			try{
				String responseMessage = jsonResponse.getString("message");
				MyLog.log("Attributes aren't valid. Message: " +responseMessage, MyLog.logMessageType.ERROR, this.getClass().getName());
			}
			catch (JSONException ex){
				String subjectID = jsonResponse.getString("key_tenant");
				String ownerID = jsonResponse.getString("device_tenant");
				boolean permissionRead = jsonResponse.getBoolean("key_read_permission");
				boolean permissionWrite = jsonResponse.getBoolean("key_write_permission");		
				
				AttributeFactory attributeFactory = AttributeFactory.getInstance();
				
				this.listAttributesLoaded.add(attributeFactory.createAttribute(SmartieXACML.URN_SUBJECT_CATEGORY_ACCESS_SUBJECT, SmartieXACML.URN_ATTRIBUTE_ID_SUBJECT_PREFIX+"subject-id", SmartieXACML.DATATYPE_STRING, subjectID));
				this.listAttributesLoaded.add(attributeFactory.createAttribute(SmartieXACML.URN_CATEGORY_RESOURCE, SmartieXACML.URN_ATTRIBUTE_ID_RESOURCE_PREFIX+"owner-id", SmartieXACML.DATATYPE_STRING, ownerID));
				this.listAttributesLoaded.add(attributeFactory.createAttribute(SmartieXACML.URN_SUBJECT_CATEGORY_ACCESS_SUBJECT, SmartieXACML.URN_ATTRIBUTE_ID_SUBJECT_PREFIX+"permission-read", SmartieXACML.DATATYPE_BOOLEAN, String.valueOf(permissionRead)));
				this.listAttributesLoaded.add(attributeFactory.createAttribute(SmartieXACML.URN_SUBJECT_CATEGORY_ACCESS_SUBJECT, SmartieXACML.URN_ATTRIBUTE_ID_SUBJECT_PREFIX+"permission-write", SmartieXACML.DATATYPE_BOOLEAN, String.valueOf(permissionWrite)));
			}
		} catch (PIPException e) {
			MyLog.log("Required attribute values weren't provided.", MyLog.logMessageType.ERROR, this.getClass().getName(), e);
		} catch (DataTypeException e) {
			MyLog.log("Error occured while converting data types.", MyLog.logMessageType.ERROR, this.getClass().getName(), e);
		} catch (FactoryException e) {
			MyLog.log("Error loading provided attributes", MyLog.logMessageType.ERROR, this.getName(), e);
		} catch (JSONException e) {
			MyLog.log("Rest service resposnse JSONObject doesn't have required fields", MyLog.logMessageType.ERROR, this.getClass().getName(), e);
		}	
	}

	@Override
	public PIPResponse getAttributes(PIPRequest pipRequest, PIPFinder pipFinder) throws PIPException {
		this.loadAttributes(pipRequest, pipFinder);
		return getAttributesFromLoaddedList(pipRequest, pipFinder);
	}

	@Override
	public void configure(String id, Properties properties) throws PIPException {
		this.name	= id;
		this.description	= properties.getProperty(id + PROP_DESCRIPTION);
		if (this.description == null) {
			this.description	= "PIPEngine for loading attributes via external REST service.";
		}
		this.loadAttributesProvided();
		this.loadAttributesRequired();
	}

}

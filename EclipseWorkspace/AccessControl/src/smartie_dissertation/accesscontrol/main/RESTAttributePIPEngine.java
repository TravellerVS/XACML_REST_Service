package smartie_dissertation.accesscontrol.main;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;


import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.api.pip.PIPResponse;

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
//		this.listAttributesRequired	= new ArrayList<Attribute>();
	}	
	@Override
	protected void loadAttributesProvided(){
//		Set<PIPRequest> returnSet = new HashSet<PIPRequest>();
//		this.listAttributesProvided = new ArrayList<Attribute>();
//		for(Attribute attribute : this.listAttributesLoaded){
//			this.listAttributesProvided.add(attribute);
//		}
	}
	
	private void loadAttributes() {		
//		AttributeDataDBManager dbManager = DBManagerFactory.getDataManager(AttributeDataDBManager.class);
//		this.listAttributesLoaded = dbManager.fetchAllEnvironmentAttributes();
	}

	@Override
	public PIPResponse getAttributes(PIPRequest pipRequest, PIPFinder pipFinder) throws PIPException {
		if(this.listAttributesLoaded.isEmpty()){
			this.loadAttributes();
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
		this.loadAttributes();
		this.loadAttributesProvided();
		this.loadAttributesRequired();
	}

}

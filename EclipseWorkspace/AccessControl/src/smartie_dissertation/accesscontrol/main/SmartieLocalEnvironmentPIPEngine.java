package smartie_dissertation.accesscontrol.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import smartie_dissertation.helperlib.XACML.AttributeFactory;
import smartie_dissertation.helperlib.XACML.SmartieXACML;
import smartie_dissertation.helperlib.log.MyLog;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.api.pip.PIPResponse;
import com.att.research.xacml.std.datatypes.DataTypeDate;
import com.att.research.xacml.std.datatypes.DataTypeDateTime;
import com.att.research.xacml.std.datatypes.DataTypeTime;
import com.att.research.xacml.util.FactoryException;

public class SmartieLocalEnvironmentPIPEngine extends SmartiePIPEngine {
	
	private static final String ENVIRONMENT_ATTRIBUTE_CURRENT_DATE 		= SmartieXACML.URN_ATTRIBUTE_ID_ENVIRONMENT_PREFIX + SmartieXACML.CURRENT_DATE;
	private static final String ENVIRONMENT_ATTRIBUTE_CURRENT_TIME		= SmartieXACML.URN_ATTRIBUTE_ID_ENVIRONMENT_PREFIX + SmartieXACML.CURRENT_TIME;
	private static final String ENVIRONMENT_ATTRIBUTE_CURRENT_DATETIME 	= SmartieXACML.URN_ATTRIBUTE_ID_ENVIRONMENT_PREFIX + SmartieXACML.CURRENT_DATETIME;
	
	public SmartieLocalEnvironmentPIPEngine() {
		
	}
	@Override
	protected void loadAttributesRequired() {
		this.listAttributesRequired	= new ArrayList<Attribute>();
	}	
	@Override
	protected void loadAttributesProvided(){
		this.listAttributesProvided	= new ArrayList<Attribute>();
		AttributeFactory attributeFactory = AttributeFactory.getInstance();
		try {
			this.listAttributesProvided.add(attributeFactory.createAttribute(SmartieXACML.URN_CATEGORY_ENVIRONMENT, ENVIRONMENT_ATTRIBUTE_CURRENT_DATE, SmartieXACML.DATATYPE_DATE));
			this.listAttributesProvided.add(attributeFactory.createAttribute(SmartieXACML.URN_CATEGORY_ENVIRONMENT, ENVIRONMENT_ATTRIBUTE_CURRENT_TIME, SmartieXACML.DATATYPE_TIME));
			this.listAttributesProvided.add(attributeFactory.createAttribute(SmartieXACML.URN_CATEGORY_ENVIRONMENT, ENVIRONMENT_ATTRIBUTE_CURRENT_DATETIME, SmartieXACML.DATATYPE_DATETIME));
		} catch (FactoryException e) {
			MyLog.log("Error loading provided attributes", MyLog.logMessageType.ERROR, this.getName(), e);
		}
	}
	
	private void loadAttributes() {
		Date currentDate = new Date();
		String xmlDate = null;
		String xmlTime = null;
		String xmlDateTime = null;
		try {
			xmlDate = DataTypeDate.newInstance().convert(currentDate).toString();
			xmlTime = DataTypeTime.newInstance().convert(currentDate).toString();
			xmlDateTime = DataTypeDateTime.newInstance().convert(currentDate).toString();
			
			this.store(SmartieXACML.URN_CATEGORY_ENVIRONMENT, ENVIRONMENT_ATTRIBUTE_CURRENT_DATE, SmartieXACML.DATATYPE_DATE , xmlDate);
			this.store(SmartieXACML.URN_CATEGORY_ENVIRONMENT, ENVIRONMENT_ATTRIBUTE_CURRENT_TIME, SmartieXACML.DATATYPE_TIME , xmlTime);
			this.store(SmartieXACML.URN_CATEGORY_ENVIRONMENT, ENVIRONMENT_ATTRIBUTE_CURRENT_DATETIME, SmartieXACML.DATATYPE_DATETIME , xmlDateTime);
		} catch (FactoryException e) {
			MyLog.log("Error creating and storing environmental attributes.", MyLog.logMessageType.ERROR, this.getName(), e);
		}
		catch (DataTypeException e) {
			MyLog.log("Error creating and converting dates", MyLog.logMessageType.ERROR, this.getName(), e);
		}
		
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
			this.description	= "PIPEngine for loading local environmental attributes";
		}
		this.loadAttributes();
		this.loadAttributesProvided();
		this.loadAttributesRequired();
	}
}

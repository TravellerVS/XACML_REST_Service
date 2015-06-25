package smartie_dissertation.accesscontrol.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import smartie_dissertation.helperlib.XACML.AttributeFactory;
import smartie_dissertation.helperlib.log.MyLog;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.api.pip.PIPEngine;
import com.att.research.xacml.api.pip.PIPException;
import com.att.research.xacml.api.pip.PIPFinder;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.api.pip.PIPResponse;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.std.datatypes.DataTypes;
import com.att.research.xacml.std.datatypes.ISO8601Date;
import com.att.research.xacml.std.datatypes.ISO8601DateTime;
import com.att.research.xacml.std.pip.StdPIPRequest;
import com.att.research.xacml.std.pip.StdPIPResponse;
import com.att.research.xacml.std.pip.engines.ConfigurableEngine;
import com.att.research.xacml.util.FactoryException;

abstract class SmartiePIPEngine implements ConfigurableEngine {
	public static final String PROP_DESCRIPTION	= ".description";
	
	protected String name;
	protected String description;
	protected List<Attribute> listAttributesLoaded		= new ArrayList<Attribute>();
	protected List<Attribute> listAttributesProvided	= new ArrayList<Attribute>();
	protected List<Attribute> listAttributesRequired	= new ArrayList<Attribute>();
	
	protected Map<String,PIPResponse> cache	= new HashMap<String,PIPResponse>();
	
//	private DataTypeFactory dataTypeFactory;
	
	protected abstract void loadAttributesRequired();
	
	protected abstract void loadAttributesProvided();
	
//	protected DataTypeFactory getDataTypeFactory() throws FactoryException {
//		if (this.dataTypeFactory == null) {
//			this.dataTypeFactory	= DataTypeFactory.newInstance();
//		}
//		return this.dataTypeFactory;
//	}
	
	protected static String generateKey(PIPRequest pipRequest) {
		StringBuilder stringBuilder	= new StringBuilder(pipRequest.getCategory().toString());
		stringBuilder.append('+');
		stringBuilder.append(pipRequest.getAttributeId().toString());
		stringBuilder.append('+');
		stringBuilder.append(pipRequest.getDataTypeId().toString());
		String issuer	= pipRequest.getIssuer();
		if (issuer != null) {
			stringBuilder.append('+');
			stringBuilder.append(issuer);
		}
		return stringBuilder.toString();
	}
	
	protected void store(String identifierCategoryField, String identifierAttributeField, String identifierDataTypeField, String valueField) throws FactoryException {
		store(identifierCategoryField, identifierAttributeField, identifierDataTypeField, valueField, null);
	}

	protected void store(String identifierCategoryField, String identifierAttributeField, String identifierDataTypeField, String valueField, String issuerField) throws FactoryException {
		Attribute attribute					= AttributeFactory.getInstance().createAttribute(identifierCategoryField, identifierAttributeField, identifierDataTypeField, valueField, issuerField);
		this.listAttributesLoaded.add(attribute);
	}
	
	
	protected HashMap<PIPRequest, AttributeValue<?>> getRequiredAttributeValues(PIPRequest pipRequest, PIPFinder pipFinder) throws PIPException {
		Collection<PIPRequest> requiredAttributes =  attributesRequired();
		HashMap<PIPRequest, AttributeValue<?>> resultAttributeValues = new HashMap<>();
		if (!requiredAttributes.isEmpty()) {
			Iterator<PIPRequest> raItter = requiredAttributes.iterator();
			while(raItter.hasNext()){
				PIPRequest pipRequestParameter = raItter.next();
				PIPResponse pipResponse	= pipFinder.getMatchingAttributes(pipRequestParameter, null);
				if (pipResponse.getStatus() == null || pipResponse.getStatus().isOk()) {
					Collection<Attribute> listAttributes	= pipResponse.getAttributes();
					if (listAttributes.size() > 0) {
						if (listAttributes.size() > 1) {
							PIPException e = new PIPException("PIPFinder returned more than one Attribute for " + pipRequestParameter.toString());
							MyLog.log("Error occured fetching additional attributes for request" , MyLog.logMessageType.ERROR, this.getClass().getName(), e);
							throw e;
						}
						Collection<AttributeValue<?>> listAttributeValuesReturned	= listAttributes.iterator().next().getValues();
						if (listAttributeValuesReturned.size() > 0) {
							if (listAttributeValuesReturned.size() > 1) {
								MyLog.log("PIPFinder returned more than one AttributeValue for " + pipRequestParameter.toString() , MyLog.logMessageType.ERROR, this.getClass().getName());
								return null;
							}
							AttributeValue<?> attributeValue			= listAttributeValuesReturned.iterator().next();
							resultAttributeValues.put(pipRequestParameter, attributeValue);
//							Identifier identifierAttributeValueDataType	= attributeValue.getDataTypeId();
//							try {
//								if (identifierAttributeValueDataType.equals(XACML3.ID_DATATYPE_INTEGER)) {
//									preparedStatement.setInt(i+1, DataTypes.DT_INTEGER.convert(attributeValue.getValue()).intValue());
//								} else if (identifierAttributeValueDataType.equals(XACML3.ID_DATATYPE_DOUBLE)) {
//									preparedStatement.setDouble(i+1, DataTypes.DT_DOUBLE.convert(attributeValue.getValue()));
//								} else if (identifierAttributeValueDataType.equals(XACML3.ID_DATATYPE_BOOLEAN)) {
//									preparedStatement.setBoolean(i+1, DataTypes.DT_BOOLEAN.convert(attributeValue.getValue()));
//								} else if (identifierAttributeValueDataType.equals(XACML3.ID_DATATYPE_DATETIME)) {
//									ISO8601DateTime iso8601DateTime	= DataTypes.DT_DATETIME.convert(attributeValue.getValue());
//									java.sql.Date sqlDate			= new java.sql.Date(iso8601DateTime.getCalendar().getTimeInMillis());
//									preparedStatement.setDate(i+1, sqlDate, iso8601DateTime.getCalendar());
//								} else if (identifierAttributeValueDataType.equals(XACML3.ID_DATATYPE_DATE)) {
//									ISO8601Date iso8601Date	= DataTypes.DT_DATE.convert(attributeValue.getValue());
//									java.sql.Date sqlDate			= new java.sql.Date(iso8601Date.getCalendar().getTimeInMillis());
//									preparedStatement.setDate(i+1, sqlDate, iso8601Date.getCalendar());
//								} else {
//									preparedStatement.setString(i+1, DataTypes.DT_STRING.convert(attributeValue.getValue()));
//								}
//							} catch (Exception ex) {
//								MyLog.log("Exception setting parameter " + (i+1) + " to " + attributeValue.toString() + ": " + ex.toString() , MyLog.logMessageType.ERROR, this.getClass().getName(), ex);
//								return null;
//							}
						} else {
							MyLog.log("No AttributeValues returned for parameter " + pipRequestParameter.toString() , MyLog.logMessageType.ERROR, this.getClass().getName());
							return null;
						}
					} else {
						MyLog.log("No Attributes returned for parameter " + pipRequestParameter.toString() , MyLog.logMessageType.ERROR, this.getClass().getName());
						return null;
					}
				} else {
					MyLog.log("PIPFinder returned status " + pipResponse.getStatus().toString() , MyLog.logMessageType.ERROR, this.getClass().getName());
					return null;
				}
			}
		}
		if (resultAttributeValues.size() <= 0) {
			MyLog.log("No matching attributes were found in the provided request." , MyLog.logMessageType.ERROR, this.getClass().getName());
		}
		return resultAttributeValues;
	}
	
	
	
	/*
	 * depricated
	 * */
//	protected Attribute findAttribute(PIPRequest pipRequest) {
//		return findAttribute(pipRequest, this.listAttributesLoaded);
//	}
//	
	protected static Attribute findAttribute(PIPRequest pipRequest, List<Attribute> listAttributes) {
		Attribute attributeResult			= null;
		Iterator<Attribute> iterAttributes	= listAttributes.iterator();
		while ((attributeResult == null) && iterAttributes.hasNext()) {
			Attribute attributeTest	= iterAttributes.next();
			if (pipRequest.getCategory().equals(attributeTest.getCategory()) &&
				pipRequest.getAttributeId().equals(attributeTest.getAttributeId()) &&
				(pipRequest.getIssuer() == null || pipRequest.getIssuer().equals(attributeTest.getIssuer()))) {
				attributeResult	= attributeTest;
			}
		}
		return attributeResult;
	}
	
	protected PIPResponse getAttributesFromLoaddedList(PIPRequest pipRequest, PIPFinder pipFinder) throws PIPException {
		String pipRequestKey	= generateKey(pipRequest);
		PIPResponse pipResponse	= this.cache.get(pipRequestKey);
		if (pipResponse != null) {
			return pipResponse;
		}
		Attribute attributeMatch	= findAttribute(pipRequest, this.listAttributesLoaded);
		if (attributeMatch == null) {
			return StdPIPResponse.PIP_RESPONSE_EMPTY;
		}
		/*
		 * Iterate through the values and only return the ones that match the requested data type
		 */
		List<AttributeValue<?>> matchingValues	= new ArrayList<AttributeValue<?>>();
		Iterator<AttributeValue<?>> iterAttributeValues	= attributeMatch.getValues().iterator();
		while (iterAttributeValues.hasNext()) {
			AttributeValue<?> attributeValue	= iterAttributeValues.next();
			if (pipRequest.getDataTypeId().equals(attributeValue.getDataTypeId())) {
				matchingValues.add(attributeValue);
			}
		}
		if (matchingValues.size() > 0) {
			Attribute attributeResponse	= new StdMutableAttribute(attributeMatch.getCategory(), attributeMatch.getAttributeId(), matchingValues, attributeMatch.getIssuer(), false);
			pipResponse					= new StdPIPResponse(attributeResponse);
			this.cache.put(pipRequestKey, pipResponse);
		}
		return pipResponse;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}
	
	@Override
	public Collection<PIPRequest> attributesRequired() {
		if(this.listAttributesProvided.isEmpty()){
			loadAttributesRequired();
		}
		return AttributeListToRequestSet(this.listAttributesRequired);
	}
	
	@Override
	public Collection<PIPRequest> attributesProvided() {
		if(this.listAttributesProvided.isEmpty()){
			loadAttributesProvided();
		}
		return AttributeListToRequestSet(this.listAttributesProvided);
	}
	
	protected static Set<PIPRequest> AttributeListToRequestSet(List<Attribute> attributeList){
		Set<PIPRequest> returnSet = new HashSet<PIPRequest>();
		for(Attribute attribute : attributeList){
			returnSet.add(new StdPIPRequest(attribute));					
		}
		return returnSet;
	}	
}

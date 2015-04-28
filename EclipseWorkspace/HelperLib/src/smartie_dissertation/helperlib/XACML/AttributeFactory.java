package smartie_dissertation.helperlib.XACML;

import smartie_dissertation.helperlib.log.MyLog;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;
import com.att.research.xacml.api.DataType;
import com.att.research.xacml.api.DataTypeException;
import com.att.research.xacml.api.DataTypeFactory;
import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.std.IdentifierImpl;
import com.att.research.xacml.std.StdMutableAttribute;
import com.att.research.xacml.util.FactoryException;

public class AttributeFactory {
	
	private DataTypeFactory dataTypeFactory;
		
	protected DataTypeFactory getDataTypeFactory() throws FactoryException {
		if (this.dataTypeFactory == null) {
			this.dataTypeFactory	= DataTypeFactory.newInstance();
		}
		return this.dataTypeFactory;
	}
	
	private static AttributeFactory instance = null;
	
	private AttributeFactory(){
		super();
	}
	
	public static AttributeFactory getInstance(){
		if(instance == null){
			instance = new AttributeFactory();
		}
		return instance;
	}
	
	public Attribute createAttribute(String identifierCategoryField, String identifierAttributeField, String identifierDataTypeField) throws FactoryException{
		return createAttribute(identifierCategoryField, identifierAttributeField, identifierDataTypeField, null);
	}
	
	public Attribute createAttribute(String identifierCategoryField, String identifierAttributeField, String identifierDataTypeField, String valueField) throws FactoryException{
		return createAttribute(identifierCategoryField, identifierAttributeField, identifierDataTypeField, valueField, null);
	}
	
	public Attribute createAttribute(String identifierCategoryField, String identifierAttributeField, String identifierDataTypeField, String valueField, String issuerField) throws FactoryException {
		DataTypeFactory thisDataTypeFactory	= this.getDataTypeFactory();
		Identifier identifierCategory		= new IdentifierImpl(identifierCategoryField);
		Identifier identifierAttribute		= new IdentifierImpl(identifierAttributeField);
		Identifier identifierDataType		= new IdentifierImpl(identifierDataTypeField);
		String value						= valueField;
		String issuer						= issuerField;		
		
		DataType<?> dataType				= thisDataTypeFactory.getDataType(identifierDataType);
		if (dataType == null) {
			MyLog.log("Unknown data type " + identifierDataType.stringValue(), MyLog.logMessageType.ERROR, this.getClass().getName());
			return null;
		}
		
		AttributeValue<?> attributeValue	= null;
		try {
			attributeValue	= dataType.createAttributeValue(value);
		} catch (DataTypeException ex) {
			throw new FactoryException("DataTypeException creating AttributeValue", ex);
		}
		Attribute attribute					= new StdMutableAttribute(identifierCategory, identifierAttribute, attributeValue, issuer, false);
		return attribute;
	}	
}

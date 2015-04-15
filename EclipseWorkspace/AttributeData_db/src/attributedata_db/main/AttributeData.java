package attributedata_db.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.DataFormatException;

import smartie_dissertation.helperlib.log.MyLog;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.AttributeValue;

public class AttributeData {
	
	public String category;
	public String attributeID;
	public String dataType;
	public String value;
	public String issuer;
	
	public AttributeData(String category, String attributeID, String dataType, String value, String issuer){
		this.category = category;
		this.attributeID = attributeID;
		this.dataType = dataType;
		this.value = value;
		this.issuer = issuer;
	}	
	
	public AttributeData(String category, String attributeID, String dataType, String value){
		this(category, attributeID, dataType, value, null);
	}
	
	public static AttributeData createAttributeData(Attribute attribute){
		Collection<AttributeValue<?>> values = attribute.getValues();
		String attributeDataType = null;
		String attributeValue = null;
		if(values.size()==1){
			ArrayList<String> attributeDataTypes = new ArrayList<>();
			ArrayList<String> attributeValues = new ArrayList<>(); 
			for(AttributeValue<?> value :values){
				attributeDataTypes.add(value.getDataTypeId().stringValue());
				//TODO something's wrong with this conversion from value to string
				attributeValues.add(value.getValue().toString());
			}
			attributeDataType = attributeDataTypes.get(0);
			attributeValue = attributeValues.get(0);
		}
		else{
			MyLog.log("Attribute has wrong number of values: "+values.size(), MyLog.logMessageType.ERROR, AttributeData.class.getName(), new DataFormatException("Attribute has wrong number of values: "+values.size()));
			return null;
//			throw new DataFormatException("Attribute has wrong number of values: "+values.size());
		}
		return new AttributeData(attribute.getCategory().toString(), attribute.getAttributeId().stringValue(), attributeDataType, attributeValue, attribute.getIssuer() );
	}
}

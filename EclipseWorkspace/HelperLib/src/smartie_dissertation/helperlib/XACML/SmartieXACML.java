package smartie_dissertation.helperlib.XACML;

import com.att.research.xacml.api.XACML;

public class SmartieXACML extends XACML{
	protected SmartieXACML() {
	}
	public static final String	URN_XACML3					= "urn:oasis:names:tc:xacml:3.0";
	public static final String	URN_XACML1					= "urn:oasis:names:tc:xacml:1.0";
	
	public static final String	URN_ATTRIBUTE_CATEGORY				= URN_XACML3+":"+XACML.ATTRIBUTE_CATEGORY;
	public static final String	URN_SUBJECT_CATEGORY				= URN_XACML1+":"+XACML.SUBJECT_CATEGORY;
	public static final String	URN_CATEGORY_RESOURCE		= URN_ATTRIBUTE_CATEGORY+":"+XACML.RESOURCE;
	public static final String	URN_CATEGORY_ENVIRONMENT	= URN_ATTRIBUTE_CATEGORY+":"+XACML.ENVIRONMENT;
	public static final String	URN_CATEGORY_ACTION		= URN_ATTRIBUTE_CATEGORY+":"+XACML.ACTION;
	
	public static final String	URN_ATTRIBUTE_ID_ENVIRONMENT_PREFIX		= URN_XACML1+":"+XACML.ENVIRONMENT+":";
	
	public static final String	CURRENT_DATE = "current-date";
	public static final String	CURRENT_TIME = "current-time";
	public static final String	CURRENT_DATETIME = "current-dateTime";
	
	public static final String	URN_SUBJECT_CATEGORY_ACCESS_SUBJECT = URN_SUBJECT_CATEGORY+":"+"access-subject";
}

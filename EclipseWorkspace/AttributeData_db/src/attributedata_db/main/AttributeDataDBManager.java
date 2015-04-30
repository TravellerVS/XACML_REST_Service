package attributedata_db.main;

import java.util.ArrayList;
import java.util.List;

import sensordata_db.cassandra.Cassandra;
import smartie_dissertation.helperlib.XACML.AttributeFactory;
import smartie_dissertation.helperlib.XACML.SmartieXACML;
import smartie_dissertation.helperlib.database.DataManager;
import smartie_dissertation.helperlib.log.MyLog;

import com.att.research.xacml.api.Attribute;
import com.att.research.xacml.api.pip.PIPRequest;
import com.att.research.xacml.util.FactoryException;
import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

//TODO: Change this to store and fetch environment attributes

/**
 * @author Vedran Semenski
 */
public class AttributeDataDBManager extends DataManager {
		
	public static enum AttributeType {
	    ENVIRONMENT, SUBJECT, RESOURCE
	}
	private final String keyspace = "attribute_data";
	private final String environment_data_table = "environment_data";
	private final String resource_data_table = "resource_data";
	private final String subject_data_table = "subject_data";
//	private final String action_data_table = "action_data";
	
	/* (non-Javadoc)
	 * @see helperlib.database.DataManager#initialize()
	 */
	@Override
	protected void initialize(){
		getCassandra().initialize();
		configureDB();
	}
	
	private Cassandra getCassandra(){
		return Cassandra.getInstance();
	}
	
	/**
	 * Closes the connection with the database and destroys the db object.
	 */
	/* (non-Javadoc)
	 * @see helperlib.database.DataManager#close()
	 */
	@Override
	public void close(){
		getCassandra().close();
		super.close();
	}
	
	/**
	 * Configures the database.
	 * This consists of checking of the DB schema and connecting to the keyspace
	 */
	/* (non-Javadoc)
	 * @see helperlib.database.DataManager#configureDB()
	 */
	@Override
	protected void configureDB(){
		//TODO remove Drop command upon every database schema change
//		dropKeyspace(keyspace);
		createSchema();
		getCassandra().use_keyspace(keyspace);
		//TODO remove generate random data
//		generateData();
	}
	
	private void dropKeyspace(String targetKeyspace){
		getCassandra().executeQuery("DROP KEYSPACE IF EXISTS "+targetKeyspace);
	}
		
	/**
	 * Creates a keyspace and tables if they don't exist already. 
	 * Changing or altering of the tables has to be done either manually or the tables have to be dropped manually before calling this.
	 */
	private void createSchema(){
		/*CREATE keyspace */
		getCassandra().executeQuery(
				"CREATE KEYSPACE IF NOT EXISTS "+keyspace+ " "
				+ "WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }"
				+ "");
		/*connect to keyspace */
		getCassandra().use_keyspace(keyspace);
//		dbClient.executeQuery("USE "+keyspace+ ";");
		
		/*CREATE tables */
		getCassandra().executeQuery("CREATE TABLE IF NOT EXISTS " + environment_data_table + " (" +
	            "category text," + 
	            "attributeID text," + 
	            "dataType text," + 
	            "value text, " +
	            "PRIMARY KEY (category, attributeID, dataType)" + 
	            ")"
	            + "");
		
		/*CREATE tables */
		getCassandra().executeQuery("CREATE TABLE IF NOT EXISTS " + subject_data_table + " (" +
				"id text," +
	            "category text," + 
	            "attributeID text," + 
	            "dataType text," + 
	            "value text, " +
	            "PRIMARY KEY (id, category, attributeID, dataType)" + 
	            ")"
	            + "");
		
		/*CREATE tables */
		getCassandra().executeQuery("CREATE TABLE IF NOT EXISTS " + resource_data_table + " (" +
				"id text," +
	            "category text," + 
	            "attributeID text," + 
	            "dataType text," + 
	            "value text, " +
	            "PRIMARY KEY (id, category, attributeID, dataType)" + 
	            ")"
	            + "");
		
//		cassandra.executeQuery(makeAttributeTableSchema(action_data_table));	
		/*return to base keyspace*/
		
		getCassandra().use_keyspace();
	}
	
	/**
	 * Generates test environment data
	 */
	public void generateData(){
		AttributeData a1 = new AttributeData(SmartieXACML.URN_CATEGORY_ENVIRONMENT, SmartieXACML.URN_ATTRIBUTE_ID_ENVIRONMENT_PREFIX+"age-limit", SmartieXACML.DATATYPE_INTEGER, "18");

		BatchStatement batch = new BatchStatement(BatchStatement.Type.UNLOGGED);
		String statementString = "INSERT INTO " + environment_data_table + " (category, attributeID, dataType, value) "
					                + "VALUES (?, ?, ?, ?)"
					                + ";";
		PreparedStatement preparedInsertStatement = getCassandra().getSession().prepare(statementString);
		batch.add(preparedInsertStatement.bind(a1.category, a1.attributeID, a1.dataType, a1.value));
				
		getCassandra().getSession().execute(batch);		
	}
	
	/**
	 * Inserts sensor data in database from the provided ArrayList of Sensor objects
	 * @param attributes - Array off attributes to be inserted
	 */
	public void insertAttributeData(ArrayList<Attribute> attributes){
		BatchStatement batch = new BatchStatement(BatchStatement.Type.UNLOGGED);
		String statementString = "INSERT INTO " + environment_data_table + " (category, attributeID, dataType, value) "
					                + "VALUES (?, ?, ?, ?)"
					                + ";";
		PreparedStatement preparedInsertStatement = getCassandra().getSession().prepare(statementString);
		for(Attribute attribute : attributes){
			AttributeData attributeData = AttributeData.createAttributeData(attribute);
			batch.add(preparedInsertStatement.bind(attributeData.category, attributeData.attributeID, attributeData.dataType, attributeData.value));
		}
		getCassandra().getSession().execute(batch);		
	}
	
	/**
	 * Prints out all data from the sensor_data_table on the standard output.
	 */
	public void printAllData(){
		ResultSet resultSet = fetchAllData(environment_data_table);
		for(Row row : resultSet.all()){
			MyLog.log(row.toString(), MyLog.logMessageType.DEBUG, this.getClass().getName());
		}
	}
	
	private ResultSet fetchAllData(String data_table){
		String query = "SELECT * FROM " + environment_data_table + "";
		return getCassandra().getSession().execute(query);
	}
	
	private List<Attribute> ResultSetToAttributeList(ResultSet resultSet){
		List<Attribute> attributeList = new ArrayList<>();
		AttributeFactory attributeFactory = AttributeFactory.getInstance();
		for(Row row : resultSet.all()){
			try {
				attributeList.add(attributeFactory.createAttribute(row.getString("category"), row.getString("attributeID"), row.getString("dataType"), row.getString("value")));
			} catch (FactoryException e) {
				MyLog.log(": "+row.toString(), MyLog.logMessageType.ERROR, this.getClass().getName(), e);
			}
		}
		return attributeList;
	}
	
	/**
	 * @return list of attributes retrieved from the database
	 */
	public List<Attribute> fetchAllEnvironmentAttributes()
	{
		ResultSet resultSet = fetchAllData(environment_data_table);
		return ResultSetToAttributeList(resultSet);
	}
	
	/**
	 * @return list of attributes retrieved from the database
	 */
	public List<Attribute> fetchAttributes(PIPRequest request)
	{
		if(!request.getCategory().stringValue().equals(SmartieXACML.URN_CATEGORY_ENVIRONMENT)){
			MyLog.log("The request is for an attribute with the wrong category.", MyLog.logMessageType.ERROR, this.getClass().getName());
			return null;
		}
		Statement select = QueryBuilder.select().all()
											.from(environment_data_table)
											.where(	QueryBuilder.eq("category", request.getCategory().stringValue()))
													.and(QueryBuilder.eq("attributeID", request.getAttributeId().stringValue()))
													.and(QueryBuilder.eq("dataType", request.getDataTypeId().stringValue()));			
		ResultSet resultSet = getCassandra().getSession().execute(select);
		return ResultSetToAttributeList(resultSet);
	}
	
	//TODO test
	/**
	 * @return list of attributes retrieved from the database
	 */
	public List<Attribute> fetchAttributes(PIPRequest request, String id)
	{
		String dataTable = "";
		if(request.getCategory().stringValue().equals(SmartieXACML.URN_SUBJECT_CATEGORY_ACCESS_SUBJECT)){
			dataTable = subject_data_table;
		}
		else if(request.getCategory().stringValue().equals(SmartieXACML.URN_CATEGORY_RESOURCE))
		{
			dataTable = resource_data_table;
		}else
		{
			MyLog.log("The request is for an attribute with the wrong category.", MyLog.logMessageType.ERROR, this.getClass().getName());
			return null;
		}
		Statement select = QueryBuilder.select().all()
											.from(dataTable)
											.where(	QueryBuilder.eq("id", id))
													.and(QueryBuilder.eq("category", request.getCategory().stringValue()))
													.and(QueryBuilder.eq("attributeID", request.getAttributeId().stringValue()))
													.and(QueryBuilder.eq("dataType", request.getDataTypeId().stringValue()));			
		ResultSet resultSet = getCassandra().getSession().execute(select);
		return ResultSetToAttributeList(resultSet);
	}
}

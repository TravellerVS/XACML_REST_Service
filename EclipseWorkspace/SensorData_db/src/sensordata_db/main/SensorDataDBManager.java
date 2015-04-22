package sensordata_db.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import sensordata_db.cassandra.Cassandra;
import smartie_dissertation.helperlib.database.DataManager;
import smartie_dissertation.helperlib.log.MyLog;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

//TODO: Make fetch functions for fetching data

/**
 * @author Vedran Semenski
 * 
 * Provides an interface for storing and fetching sensor_data.
 * It uses a Cassandra DB.
 *
 */
public class SensorDataDBManager extends DataManager {
	
	private static SensorDataDBManager instance = null;
	
	private SensorDataDBManager(){
		initialize();
	}
	
	/**
	 * @return it returns a SensorDataDB object using the singleton pattern so the instance is always the same no matter who calls it.
	 */
	public static SensorDataDBManager getInstance(){
		if(instance == null){
			instance = new SensorDataDBManager();
		}
		return instance;
	}	

	private Cassandra cassandra;	
	private final String sensor_data_table = "sensor_values";
	private final String keyspace = "sensor_data";
	private final String environment_data_table = "environment_data";
	
	
	/* (non-Javadoc)
	 * @see helperlib.database.DataManager#initialize()
	 */
	@Override
	protected void initialize(){
		cassandra = Cassandra.getInstance();
		cassandra.initialize();
		configureDB();
	}
	
	/**
	 * Closes the connection with the database and destroys the db object.
	 */
	/* (non-Javadoc)
	 * @see helperlib.database.DataManager#close()
	 */
	@Override
	public void close(){
		if(this.cassandra != null){
			this.cassandra.close();
			this.cassandra = null;
		}	
		instance=null;
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
		cassandra.use_keyspace(keyspace);
		//TODO remove generate data
//		generateSensorData();
	}
	
	private void dropKeyspace(String targetKeyspace){
		cassandra.executeQuery("DROP KEYSPACE IF EXISTS "+targetKeyspace);
	}
	
	/**
	 * Creates a keyspace and tables if they don't exist already. 
	 * Changing or altering of the tables has to be done either manually or the tables have to be dropped manually before calling this.
	 */
	private void createSchema(){
		
		cassandra.executeQuery("DROP KEYSPACE IF EXISTS "+keyspace);
		
		/*CREATE keyspace */
		cassandra.executeQuery(
				"CREATE KEYSPACE IF NOT EXISTS "+keyspace+ " "
				+ "WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }"
				+ "");
		/*connect to keyspace */
		cassandra.use_keyspace(keyspace);
//		dbClient.executeQuery("USE "+keyspace+ ";");
		
		/*CREATE user defined types */
		cassandra.executeQuery(
			      "CREATE TYPE IF NOT EXISTS sensor_location (" +
			            "x double," + 
			            "y double," + 
			            "z double," + 
			            "address text" + 
			            ")"
			            + "");		
		/*CREATE table */
		cassandra.executeQuery(
			      "CREATE TABLE IF NOT EXISTS " + sensor_data_table + " (" +
			            "sensor_id int," + 
			            "type text," + 
			            "value double," + 
			            "location frozen <sensor_location>," + 
			            "datetime timestamp,"
			            + "PRIMARY KEY (sensor_id)" + 
			            ")"
			            + "");
		/*CREATE index for table */
		cassandra.executeQuery(
			      "CREATE INDEX ON " + sensor_data_table + "(type)"
			            + "");
						
		/*return to base keyspace*/
		cassandra.use_keyspace();
	}
	
	/**
	 * Generates pseudo random sensor data and fills the database with it.
	 */
	public void generateSensorData(){
		int i = 0;
		int i_max = 10;
		int j = 0;
		int j_max = 10;
		
		double start = -10;
		double end = 40;
		Random random = new Random();
		ArrayList<Sensor> sensorData = new ArrayList<Sensor>();
		for(i = 0; i<i_max ;i++){
			for(j = 0; j<j_max ;j++){
				Location location = new Location(i, i);
				int id = i_max*i+j;
				double value = start + (random.nextDouble() * (end - start));
				Date dateTime = new Date();
				Sensor sensor = new Sensor(id, "type"+i, value, dateTime, location);
				sensorData.add(sensor);
			}
			insertSensorData(sensorData);
			sensorData.clear();
		}
	}
	
	/**
	 * Inserts sensor data in database from the provided ArrayList of Sensor objects
	 * @param sensorData - Array of Sensor objects.
	 */
	public void insertSensorData(ArrayList<Sensor> sensorData){
		BatchStatement batch = new BatchStatement(BatchStatement.Type.UNLOGGED);
		String statementString = "INSERT INTO " + sensor_data_table + " (sensor_id, type, value, location, datetime) "
					                + "VALUES (?, ?, ?, {x: ?, y: ?, z: ?, address: ?}, ?)"
					                + ";";
		PreparedStatement preparedInsertStatement = cassandra.getSession().prepare(statementString);
		for(Sensor sensor : sensorData){
			batch.add(preparedInsertStatement.bind(sensor.id, sensor.type, sensor.value, 
					sensor.location.x, sensor.location.y, sensor.location.z, sensor.location.address, 
					sensor.dateTime ));
		}
		cassandra.getSession().execute(batch);		
	}
	
	/**
	 * Prints out all data from the sensor_data_table on the standard output.
	 */
	public void printAllData(){
		String query = "SELECT * FROM " + sensor_data_table + "";
		ResultSet resultSet = cassandra.getSession().execute(query);
		for(Row row : resultSet.all()){
			MyLog.log(row.toString(), MyLog.logMessageType.DEBUG, this.getClass().getName());
//			out.printf(row.toString()+"\n");
		}
	}
		
}

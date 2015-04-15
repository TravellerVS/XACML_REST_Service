package sensordata_db.main;

import java.util.Date;


/**
 * @author Vedran Semenski
 * 
 * Class used for storing SensorData.
 * When changing this class the SensorDataDB also has to be updated.
 *
 */
public class Sensor {
	public int id;
	public String type;
	public double value;	
	public Date dateTime;
	public Location location;
		
	/**
	 * @param id - sensorID
	 * @param type - sensor type (example. temperature, humidity...)
	 * @param value - a value provided by the sensor
	 * @param dateTime - the time that the reading was taken at
	 * @param location - the location at which the reading was taken
	 */
	public Sensor(int id, String type,  double value, Date dateTime, Location location ){
		this.id = id;
		this.type = type;
		this.value = value;
		this.dateTime = dateTime;
		this.location = location;
	}
}

package sensordata_db.main;

/**
 * @author Vedran Semenski
 * 
 * Class used for storing Sensor's Location.
 * When changing this class the Sensor class also has to be updated.
 *
 */
public class Location {
	public double x;
	public double y;
	public double z;
	public String address;
	
	public Location(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	public Location(double x, double y, double z)
	{
		this(x, y);
		this.z = z;
	}
	
	public Location(String  address)
	{
		this.address = address;
	}
}

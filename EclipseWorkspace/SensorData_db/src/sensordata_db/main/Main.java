package sensordata_db.main;

import static java.lang.System.out;
import smartie_dissertation.helperlib.database.DBManagerFactory;
import smartie_dissertation.helperlib.database.DataManager;

public class Main {

	public static void main(String[] args) {
//		final String ipAddress = args.length > 0 ? args[0] : "localhost";
//		final int port = args.length > 1 ? Integer.parseInt(args[1]) : 9042;
//		out.println("Connecting to IP Address " + ipAddress + ":" + port + "...");
		SensorDataDBManager db = DBManagerFactory.getDataManager(SensorDataDBManager.class);
//		db.generateSensorData();
		db.printAllData();
		db.close();
		out.print("FINISHED");
	}
	
}

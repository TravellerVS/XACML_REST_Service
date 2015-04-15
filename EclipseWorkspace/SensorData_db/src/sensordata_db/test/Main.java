package sensordata_db.test;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class Main {

	/**
	 * Main function for demonstrating connecting to Cassandra with host and port.
	 *
	 * @param args Command-line arguments; first argument, if provided, is the
	 *    host and second argument, if provided, is the port.
	 */
	public static void main(final String[] args)
	{
	   final CassandraConnector client = new CassandraConnector();
	   final String ipAddress = args.length > 0 ? args[0] : "localhost";
	   final int port = args.length > 1 ? Integer.parseInt(args[1]) : 9042;
	   System.out.println("Connecting to IP Address " + ipAddress + ":" + port + "...");
	   client.connect(ipAddress, port);
	   
	   String query =
	  		     "USE mykeyspace; ";
	   client.getSession().execute(query);	
//	   query =	"CREATE TABLE users ( user_id int PRIMARY KEY,  fname text, lname text);";
//	   client.getSession().execute(query);	
	   query =	"INSERT INTO users (user_id,  first_name, last_name)  VALUES (1845, 'john', 'smith');";
	   client.getSession().execute(query);	
	   query =	"INSERT INTO users (user_id,  first_name, last_name)  VALUES (1844, 'john', 'doe');";
	   client.getSession().execute(query);	
	   query =	"INSERT INTO users (user_id,  first_name, last_name)  VALUES (1846, 'john', 'smith');";
	   client.getSession().execute(query);
	   query =	"SELECT * FROM users;";
	   ResultSet rs = client.getSession().execute(query);	
	   for(Row r : rs.all()){
		   System.out.println(r.toString());
	   }
	   client.close();
		
		Cluster cluster;
		Session session;
		
		// Connect to the cluster and keyspace "demo"
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("demo");
		
		// Insert one record into the users table
		session.execute("INSERT INTO users (lastname, age, city, email, firstname) VALUES ('Jones', 35, 'Austin', 'bob@example.com', 'Bob')");

		// Use select to get the user we just entered
		ResultSet results = session.execute("SELECT * FROM users WHERE lastname='Jones'");
		for (Row row : results) {
			System.out.format("%s %d\n", row.getString("firstname"), row.getInt("age"));
		}
		
		// Update the same user with a new age
		session.execute("update users set age = 36 where lastname = 'Jones'");
		// Select and show the change
		results = session.execute("select * from users where lastname='Jones'");
		for (Row row : results) {
			System.out.format("%s %d\n", row.getString("firstname"), row.getInt("age"));
		}
		
		// Delete the user from the users table
		session.execute("DELETE FROM users WHERE lastname = 'Jones'");
		// Show that the user is gone
		results = session.execute("SELECT * FROM users");
		for (Row row : results) {
			System.out.format("%s %d %s %s %s\n", row.getString("lastname"), row.getInt("age"),  row.getString("city"), row.getString("email"), row.getString("firstname"));
		}
		
		// Clean up the connection by closing it
		cluster.close();
		
	}
}

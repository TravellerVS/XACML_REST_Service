package sensordata_db.cassandra;

import smartie_dissertation.helperlib.log.MyLog;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/**
 * @author Vedran Semenski
 * 
 * Class is used as a generic interface for communicating with the CassandraDB used
 *
 */
public class Cassandra {
		
	private static Cassandra instance = null;
	
	final private String default_CassandraIpAddress = "localhost";
	final private int default_CassandraPort = 9042;	
	
	/**
	 * Empty private constructor
	 */
	private Cassandra(){
		
	}
	
	/**
	 * @return return an instance of the client object using a singleton pattern so it is always the same instance
	 */
	public static Cassandra getInstance(){
		if(instance == null)
		{
			instance = new Cassandra();
		}
		return instance;
	}
	
	/** Cassandra Cluster. */
	private Cluster cluster;

	/** Cassandra Session. */
	private Session session;

	/**
	* Connect to Cassandra Cluster specified by the default nodeIP address and port number
	*/
	public void initialize(){
		connect(this.default_CassandraIpAddress, this.default_CassandraPort);
	}
	/**
	* Connect to Cassandra Cluster specified by provided node IP
	* address and port number.
	*
	* @param node Cluster node IP address.
	* @param port Port of cluster host.
	*/
	private void connect(final String node, final int port)
	{
		this.cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
		final Metadata metadata = this.cluster.getMetadata();
		MyLog.log("Connected to cluster: "+metadata.getClusterName(), MyLog.logMessageType.INFO, this.getClass().getName());
//		out.printf("Connected to cluster: %s\n", metadata.getClusterName());
		for (final Host host : metadata.getAllHosts())
		{
			MyLog.log("Datacenter: "+ host.getDatacenter()+"; Host: "+ host.getAddress()+"; Rack: "+ host.getRack(), MyLog.logMessageType.INFO, this.getClass().getName());
//			out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
		}
		this.session = cluster.connect();
	}	
	
	/**
	 * Connects to Cassandra and initializes the session
	 */
	public void use_keyspace()
	{
		this.session = cluster.connect();
	}
	
	/**
	 * Connects to the the keyspace and reinitializes the session in the desired keyspace
	 * @param keyspace - the destination keyspace
	 */
	public void use_keyspace(String keyspace)
	{
		this.session = cluster.connect(keyspace);
	}
	/**
	* Provide my Session.
	*
	* @return My session.
	*/
	public Session getSession()
	{
		checkSession();
		return this.session;
	}
	
	/** Close Cassandra */
	public void close(){
		if(this.session != null){
			this.session.close();
			this.session = null;
		}
		if(this.cluster != null){
			this.cluster.close();
			this.cluster = null;
		}
		instance = null;
	}
	
	/**
	 * @param query - string containing the query that will be sent to the DB.
	 * @return returns ResultSet object containing the direct response from the DB.
	 * @throws Exception 
	 */
	public ResultSet executeQuery(String query){
		return this.getSession().execute(query);
	}	
	
	/**
	 * 
	 */
	private void checkSession()
	{
		try {
			validateSession();
		} catch (Exception e) {
			MyLog.log("Session isn't valid.", MyLog.logMessageType.ERROR, this.getClass().getName());
//			this.initialize();
		}
	}
	
	/**
	 * @throws Exception
	 */
	private void validateSession() throws Exception
	{
		if(this.session == null)
		{
			MyLog.log("Session isn't initialized.", MyLog.logMessageType.ERROR, this.getClass().getName());
			throw new Exception("Session isn't initialized.");
		}
	}
}

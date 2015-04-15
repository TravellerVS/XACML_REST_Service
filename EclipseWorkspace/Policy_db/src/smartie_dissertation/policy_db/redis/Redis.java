package smartie_dissertation.policy_db.redis;

import redis.clients.jedis.Jedis;
import smartie_dissertation.helperlib.log.MyLog;


public class Redis {
	
	private static Redis instance = null;
	
	/**
	 * Empty private constructor
	 */
	private Redis(){
		initialize();
	}
	
	/**
	 * @return return an instance of the client object using a singleton pattern so it is always the same instance
	 */
	public static Redis getInstance(){
		if(instance == null){
			instance = new Redis();
		}
		return instance;
	}
	
	private Jedis jedis = null;
	
	/**
	 *Initializes the connection to the Redis database. It uses a Jedis java client to communnicate t a Redis database.
	 */
	private void initialize()
	{
		try {
			this.jedis = new Jedis("localhost");
			MyLog.log("Server is running: "+this.jedis.ping(), MyLog.logMessageType.INFO, this.getClass().getName());
			MyLog.log("Connected to server sucessfully.", MyLog.logMessageType.INFO, this.getClass().getName());
//			System.out.println("Server is running: "+this.jedis.ping());
//			System.out.println("Connected to server sucessfully.");
			//check whether server is running or not
		} catch (Exception e) {
			MyLog.log("Connection not established: "+e.getMessage(), MyLog.logMessageType.ERROR, this.getClass().getName());
//			System.out.println("Connection not established: "+e.getMessage());
			this.close();
		}
	}
	
	/**
	 * Closes and deinitializes the Jedis client and the instance of manager
	 */
	public void close()
	{
		if(this.jedis != null){
			this.jedis.close();
			this.jedis = null;
		}	
		instance = null;
	}
	
	/**
	 * returns a Jedis object that can be used to communicate to a Redis databse 
	 * @return Jedis object
	 */
	public Jedis getJedis(){
		checkJedis();
		return this.jedis;		
	}
	
	/**
	 * @param key - key of the value that is going to be returned.
	 * @return value behind the key value provided in the call.
	 */
	public String getValue(String key){
		return this.getJedis().get(key);
	}

	/**
	 * @param key - key of the value that is going to be stored.
	 * @param value that is going to be stored.
	 */
	public void storeValue(String key, String value){
		this.getJedis().set(key, value);
	}
	
	/**
	 * Check if the Jedis object is valid and outputs an error message to the log if not
	 */
	private void checkJedis()
	{
		try {
			validateJedis();
		} catch (Exception e) {
			MyLog.log("Jedis object isn't valid.", MyLog.logMessageType.INFO, this.getClass().getName());
//			System.out.println("Jedis object isn't valid.");
//			this.initialize();
		}
	}
	
	/**
	 * @throws Exception - in case the Jedis object isn't valid (!=null)
	 */
	private void validateJedis() throws Exception
	{
		if(this.jedis == null){
			MyLog.log("Jedis object isn't initialized and equals null.", MyLog.logMessageType.INFO, this.getClass().getName());
			throw new Exception("Jedis object isn't initialized and equals null.");
		}
	}
	
}

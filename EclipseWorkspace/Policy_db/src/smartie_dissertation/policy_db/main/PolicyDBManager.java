package smartie_dissertation.policy_db.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import smartie_dissertation.helperlib.database.DataManager;
import smartie_dissertation.helperlib.generic.HelperFunctions;
import smartie_dissertation.helperlib.log.MyLog;
import smartie_dissertation.policy_db.redis.Redis;

/**
 * @author Vedran Semenski
 * 
 */
public class PolicyDBManager extends DataManager {
	
	private static PolicyDBManager instance = null;
	private Redis redis = null;
	
	private static final String DEFAULT_POLICY_SET_KEY = "default_policy_set";
	private static final String DEFAULT_POLICY_ID_KEY = "default_policy_id";
	private static final String DEFAULT_FREE_POLICY_ID_SET_KEY = "default_free_policy_id_set";
	/**
	 * Empty private constructor
	 */
	private PolicyDBManager(){
		initialize();
	}
	
	/**
	 * @return return an instance of the client object using a singleton pattern so it is always the same instance
	 */
	public static PolicyDBManager getInstance(){
		if(instance == null)
		{
			instance = new PolicyDBManager();
		}
		return instance;
	}
	
	/**
	 * destroys instance of the manager and closes the connections
	 */
	public static void closeConnections(){
		if(instance != null){
			instance.close();
		}
	}

	/* (non-Javadoc)
	 * @see helperlib.database.DataManager#initialize()
	 */
	@Override
	protected void initialize() {
		this.redis = Redis.getInstance();
		configureDB();
	}

	/* (non-Javadoc)
	 * @see helperlib.database.DataManager#configureDB()
	 */
	@Override
	protected void configureDB() {	
//		deleteAllPolicies();
		if(!this.redis.getJedis().exists(DEFAULT_POLICY_ID_KEY)){
			this.redis.getJedis().set(DEFAULT_POLICY_ID_KEY, "0");
		}
		if(!this.redis.getJedis().exists(DEFAULT_FREE_POLICY_ID_SET_KEY)){
			this.redis.getJedis().rpush(DEFAULT_FREE_POLICY_ID_SET_KEY, getNewPolicyID());
		}
//		if(!this.redis.getJedis().exists(DEFAULT_POLICY_SET_KEY)){
//			storePolicy(DEFAULT_POLICY_SET_KEY, "");
//		}		
	}

	/* (non-Javadoc)
	 * @see helperlib.database.DataManager#close()
	 */
	@Override
	public void close() {
		if(this.redis != null){
			this.redis.close();
			this.redis = null;
		}	
		instance = null;		
	}
	
	/**
	 * Retrieves policy file from the database.
	 * @param id - id of the policy
	 * @return policy in a string format.
	 */
	public String getPolicy(String id){
		return getPolicy(DEFAULT_POLICY_SET_KEY, id);
	}
	/**
	 * Retrieves policy file from the database.
	 * @param key - key of wanted policy set
	 * @param id - id of the policy
	 * @return policy in a string format.
	 */
	private String getPolicy(String key, String id){
		return this.redis.getJedis().hget(key, id);
	}	
	
	/**
	 * Retrieves policy files from the database.
	 * @return list of policies in a string format.
	 */
	public Map<String, String> getPolicies(){
		return getPolicies(DEFAULT_POLICY_SET_KEY);
	}	
	/**
	 * Retrieves policy file from the database.
	 * @param key - key of wanted policy set
	 * @return list of policies in the format <policy id, policy>
	 */
	private Map<String, String> getPolicies(String key){
//		Map<String, String> resultingList = new HashMap<>();		
		Map<String, String> resultingList = this.redis.getJedis().hgetAll(key);
//		for(Map.Entry<String, String> entry : policyList.entrySet()){
//			resultingList.put(Long.parseLong(entry.getKey()), entry.getValue());
//		}
		return resultingList;
	}		

	/**
	 * @return Returns new and free ID for a policy. It is in a String format converted from a long.
	 */
	private String getNewPolicyID(){
		long id = 0;
		if(this.redis.getJedis().llen(DEFAULT_FREE_POLICY_ID_SET_KEY)>0){
			id = Long.parseLong(this.redis.getJedis().lpop(DEFAULT_FREE_POLICY_ID_SET_KEY));
		}
		if(id <=0 ){
			id = this.redis.getJedis().incr(DEFAULT_POLICY_ID_KEY);
		}
		return Long.toString(id);
	}
	
	private void freePolicyID(String id){
		/*add policy id to free id set*/
		this.redis.getJedis().rpush(DEFAULT_FREE_POLICY_ID_SET_KEY, id);
	}
	
	/**
	 * Stores policy file in the database.
	 * @param policy - policy in string format.
	 */
	public String storePolicy(String policy){
		return storePolicy(DEFAULT_POLICY_SET_KEY, policy);
	}
	/**
	 * Stores policy file in the database.
	 * @param key - key on which the policy will be stored
	 * @param policy - policy in string format.
	 */
	private String storePolicy(String key, String policy){
		String newPolicyID = HelperFunctions.StringToSH1(policy);
		if( this.redis.getJedis().hset(key, newPolicyID, policy) == null ){
			newPolicyID = null;
			MyLog.log("storePolicy failed. key="+key, MyLog.logMessageType.ERROR, this.getClass().getName());
		}
		return newPolicyID;
	}	
	
	/**
	 * Copies the provided policy over the existing one.
	 * @param id - id of the policy 
	 * @param policy - string containing the new policy
	 */
	public void updatePolicy(String id, String policy){
		updatePolicy(DEFAULT_POLICY_SET_KEY, id, policy);
	}	
	/**
	 * Copies the provided policy over the existing one.
	 * @param key - key for the policy set
	 * @param id - id of the policy 
	 * @param policy - string containing the new policy
	 */
	private boolean updatePolicy(String key, String id, String policy){
		boolean result = (this.redis.getJedis().hset(key,id, policy) != null);
		if(result){
			MyLog.log("updatePolicy failed. key="+key+" id="+id, MyLog.logMessageType.ERROR, this.getClass().getName());
		}		
		return result;
	}
	
	/**
	 * Deletes everything in the database. It is a delete key operation that removes all used keys and its content.
	 */
	public void deleteAllPolicies()	{
		deleteKey(DEFAULT_POLICY_ID_KEY);
		deleteKey(DEFAULT_POLICY_SET_KEY);
		deleteKey(DEFAULT_FREE_POLICY_ID_SET_KEY);
		configureDB();
	}
	
//	/**
//	 * Stores a list of policies in the database.
//	 * @param policy - list of policies in a string format
//	 */
//	public void appendPolicy(String policy){
//		appendPolicy(DEFAULT_POLICY_SET_KEY, policy);
//	}
//	
//	/**
//	 * Stores a list of policies in the database.
//	 * @param key - key on which the list of policies will be stored
//	 * @param policy - list of policies in a string format
//	 */
//	public void appendPolicy(String key, String policy){
//		this.redis.getJedis().lpush(key, policy);
//	}
//	
//	/**
//	 * Stores a list of policies in the database.
//	 * @param key - key on which the list of policies will be stored
//	 * @param policyList - list of policies in a string format
//	 */
//	public void storePolicies(String key, List<String> policyList){
//		for(String policy : policyList){
//			appendPolicy(key, policy);
//		}
//	}
	
	/**
	 * Removes the policy from the database
	 * @param id - id of the policy that is going to be deleted
	 */
	public void removePolicy(String id){
		/*if field in hash set exists*/
		if(this.redis.getJedis().hexists(DEFAULT_POLICY_SET_KEY, id)){
			/*delete policy*/
			this.redis.getJedis().hdel(DEFAULT_POLICY_SET_KEY, id);
//			freePolicyID(id);
		}
	}
	
	/**
	 * Empties the value behind the key and removes the key
	 * @param key - key that is going to be deleted
	 */
	public void deleteKey(String key){
		this.redis.getJedis().del(key);
	}
	
	/**
	 * Outputs and returns list of keys currently set in the database:
	 * @return List of keys currently set in the database.
	 */
	public List<String> outputKeys(){
		List<String> resultingList = new ArrayList<>();
		Set<String> set = this.redis.getJedis().keys("*");
		String keys = "List of keys currently used:";
		for(String setString : set){
			keys += " "+setString;
//			System.out.println("List of stored keys:: "+setString);
			resultingList.add(setString);
		}
		MyLog.log(keys, MyLog.logMessageType.DEBUG, this.getClass().getName());
		return resultingList;
	}
}

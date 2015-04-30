package smartie_dissertation.helperlib.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import smartie_dissertation.helperlib.log.MyLog;

public class DBManagerFactory{
	
	private static DBManagerFactory instance = null;
	
	private Map<Class<? extends DataManager>, DataManager> managerList = new HashMap<>();
	
	private DBManagerFactory(){
	}
	
	private static DBManagerFactory getInstance(){
		if(instance == null){
			instance = new DBManagerFactory();
		}
		return instance;
	}	
	
	public <T extends DataManager> void removeFromList (T oldManager){
		for(Entry<Class<? extends DataManager>,? extends DataManager> entry : getInstance().managerList.entrySet()){
			if(entry.getKey()==oldManager.getClass() || entry.getValue() == null){
				DataManager instance = entry.getValue();
				instance = null;
				getInstance().managerList.remove(entry.getKey());
			}
		}		
	}
	
//	public static <T extends DataManager> T getDataManager(){
//		T resultManager = null;	
//		GenericClass<T> genericClass = new GenericClass<T>();
//		Class<T> desiredClass = genericClass.getGenericClass();
//		for(Entry<Class<? extends DataManager>,? extends DataManager> entry : getInstance().managerList.entrySet()){
//			if(entry.getValue() == null){
//				getInstance().managerList.remove(entry.getKey());
//			}else if(entry.getKey() == desiredClass){
//				resultManager = (T) entry.getValue();
//				break;
//			}
//		}
//		if(resultManager == null){
////			Class<?> c = Class.forName("mypackage.MyClass");
//			Constructor<? extends DataManager> cons;
//			try {
//				cons = desiredClass.getConstructor();
//				resultManager = (T) cons.newInstance();
//				resultManager.setHolder(getInstance()); 
//				getInstance().managerList.put(resultManager.getClass(),resultManager);		
//			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//				MyLog.log("Exception occured while creating an isstance of " + resultManager.getClass().getName() + " class. "  , MyLog.logMessageType.ERROR, DBManagerFactory.class.getName(), e);
//			}
//		}
//		return resultManager;
//	}	
	
	public static <T extends DataManager> T getDataManager(Class<? extends DataManager> desiredClass){
		T resultManager = null;	
		for(Entry<Class<? extends DataManager>,? extends DataManager> entry : getInstance().managerList.entrySet()){
			if(entry.getValue() == null){
				getInstance().managerList.remove(entry.getKey());
			}else if(entry.getKey() == desiredClass){
				resultManager = (T) entry.getValue();
				break;
			}
		}
		if(resultManager == null){
//			Class<?> c = Class.forName("mypackage.MyClass");
			Constructor<? extends DataManager> cons;
			try {
				cons = desiredClass.getConstructor();
				resultManager = (T) cons.newInstance();
				resultManager.setHolder(getInstance());
				getInstance().managerList.put(resultManager.getClass(),resultManager);		
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				MyLog.log("Exception occured while creating an isstance of " + resultManager.getClass().getName() + " class. "  , MyLog.logMessageType.ERROR, DBManagerFactory.class.getName(), e);
			}
		}
		//getInstance(); //test reasons
		return resultManager;
	}	
	
	/**
	 * destroys instance of the manager and closes the connections
	 */
	public static <T extends DataManager> void closeConnections(Class<T> desiredClass){
		T instance = getDataManager(desiredClass);
		if(instance != null){
			instance.close();
		}
	}
	
	/**
	 * destroys instance of the manager and closes the connections
	 */
	public static <T extends DataManager> void closeAllConnections(){
		for(Entry<Class<? extends DataManager>,? extends DataManager> entry : getInstance().managerList.entrySet()){
			if(entry.getValue() == null){
				entry.getValue().close();
			}
		}
	}	
}

package smartie_dissertation.helperlib.database;

/**
 * @author Vedran Semenski
 * 
 * Class is to be used as a template for implementing a DataManager
 *
 */
public abstract class DataManager {
	/**
	 * Establishes a connection with the database and calls configureDB() to make initial configurations.
	 */
	protected abstract void initialize();
	
	/**
	 * Configures the database.
	 */
	protected abstract void configureDB();
	
	/**
	 * Closes the connection with the database and destroys the Manager object.
	 */
	public abstract void close();
}

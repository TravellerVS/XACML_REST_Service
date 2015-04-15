package smartie_dissertation.helperlib.generic;

/**
 * @author Vedran Semenski
 * 
 * Class isn't used and inherited in any way. Used as a copy-paste template for the use as a standard singleton design pattern.
 *
 */
public class Singleton {
	
	private static Singleton instance = null;
	
	private Singleton(){
		super();
	}
	
	public static Singleton getInstance(){
		if(instance == null){
			instance = new Singleton();
		}
		return instance;
	}
}

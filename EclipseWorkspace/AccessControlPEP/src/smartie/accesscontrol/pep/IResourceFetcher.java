package smartie.accesscontrol.pep;

/**
 * @author Vedran Semenski
 *
 */
public interface IResourceFetcher {
	/**
	 * Implementation should execute the fetching/execution of a resource/action.
	 */
	public void execute();
	/**
	 * Implementation should execute the case of a failure. 
	 */
	public void terminate();
}

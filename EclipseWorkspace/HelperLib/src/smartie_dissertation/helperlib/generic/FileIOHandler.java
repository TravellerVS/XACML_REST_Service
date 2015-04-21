package smartie_dissertation.helperlib.generic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class providing basic FileIO functionality.
 * 
 * @author Vedran Semenski
 *
 */
public class FileIOHandler {

	/**
	 * Creates a file and directories (is necessary) according to the fullPath provided
	 * If file already exists it returns that file
	 * @param fullPath - path to the destination file including the filename and extension
	 * @return created file
	 */
	public static File getFile(String fullPath){
		File file = new File(fullPath);
		file.getParentFile().mkdirs();
		return file;
	}
	
	/**
	 * Empties the content of the file by printing an empty string in it.
	 * @param file - targeted file
	 */
	public static void emptyFile(File file){
		PrintWriter writer;
		try {
			writer = new PrintWriter(file);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			Logger.getLogger(FileIOHandler.class.getName()).log(Level.SEVERE, null, e);
		}		
	}
	
	/**
	*
	* @param file - file that is going to be read
	* @return content of file
	*/
	public static String readFromFile(File file)
	{
		try {
			return new String(Files.readAllBytes(file.toPath()), "UTF-8");
      	} catch (IOException e) {
      		Logger.getLogger(FileIOHandler.class.getName()).log(Level.SEVERE, null, e);
      	}
      	return null;
   	}
	
	/**
	* @param file - file to be deleted
	*/
	public static void deleteFile(File file){
		if (file.exists()) {
			file.delete();
		}
	}
	   
	/**
	 * @param file - destination file
	 * @param content - content to be outputed to file
	 */
	public static void outputToFile(File file, String content)
	{
		outputToFile(file, content, false); 
	}
	
	/**
	 * @param file - destination file
	 * @param content - content to be appended to file
	 * @param append - if true file is appended, if false, file is not appended
	 */
	public static void outputToFile(File file, String content, boolean append)
	{
		try {
			FileWriter fw = new FileWriter(file, append);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			Logger.getLogger(FileIOHandler.class.getName()).log(Level.SEVERE, null, e);
		}
	}
}

/**
 * 
 */
package temperatures;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jules damji
 * 
 * FileUtils, as the name suggests, is a convenience utility class that returns a list of files given a directory name.
 * Other file related may be added to extend its utility. Note, the class must be idempotent; not state is mentioned, since
 * most callers will invoke its static methods, without knowledge of its internal preserved state.
 *
 */
public class FileUtils {
	
	public static List<String> listOfFiles(String pDir) {
		
		List<String> results = new ArrayList<String>();
		File[] files = new File(pDir).listFiles();

		for (File file : files) {
		    if (file.isFile()) {
		    	String name = file.getName();
		    	if (name.startsWith(".") || name.startsWith("./")) {
		    		continue;
		    	}
		        results.add(name);
		    }
		}
		return results;
	}

}

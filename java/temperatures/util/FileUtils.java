/**
 * 
 */
package temperatures.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jules
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

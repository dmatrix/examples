/**
 * 
 */
package temperatures.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import temperatures.cdata.CityTemperatures;

/**
 * @author jules
 *
 */
public class TemperatureReaderUtil {
	
	public static CityTemperatures getCityTemperatures(String pFile) throws IOException {
		File f = new File (pFile);
		CityTemperatures cityTemps = new CityTemperatures(f.getName());
		FileReader fileReader = new FileReader(new File(pFile));
		BufferedReader br = new BufferedReader(fileReader);

		 String line = null;
		 // if no more lines the readLine() returns null
		 while ((line = br.readLine()) != null) {
			 StringTokenizer st = new StringTokenizer(line, ",");
			 if (st.countTokens() == 2) {
				 String city = st.nextToken();
				 int tmp = Integer.valueOf(st.nextToken());
				 cityTemps.add(city, tmp);
			 }
		 }
		  if (fileReader != null) {
			  fileReader.close();
		 }
		return cityTemps;
	}

}
/**
 * 
 */
package temperatures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author jules damji
 *
 * TemperatureReaderUtil is a utility class that reads recoreded temperatures from the file.
 * The format of the file is simple. Each line contains a city name and a recorded temperature,
 * separated by a ','. Note, the file may contain multiple entries for the city temperatures recorded
 * through out the day.
 */
public class TemperatureReaderUtil {
	
	public static CityTemperatures getCityTemperatures(String pFile) throws IOException {
		File f = new File (pFile);
		CityTemperatures cityTemps = new CityTemperatures(f.getName());
		FileReader fileReader = new FileReader(new File(pFile));
		BufferedReader br = new BufferedReader(fileReader);

		 String line = null;
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
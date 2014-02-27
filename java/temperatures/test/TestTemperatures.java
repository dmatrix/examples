/**
 * 
 */
package temperatures.test;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import temperatures.cdata.CityTemperatures;
import temperatures.util.*;
/**
 * @author jules
 *
 */
public class TestTemperatures {

	/**
	 * 
	 */
	public TestTemperatures() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dirName = null;
		String cityName = null;
		if (args.length == 4) {
			if (args[0].startsWith("--dir") && args[2].startsWith("--city")) {
				dirName = args[1];
				cityName = args[3];
			} else {
				System.out.println ("java TestTemperatures --dir <dirname> --city city");
				System.exit (1);
			}
		}
		Map<String, temperatures.cdata.CityTemperatures> cityTempMap = new HashMap<String, CityTemperatures>();
		List<String>files = FileUtils.listOfFiles(dirName);
		for (String f: files) {
			try {
				CityTemperatures cityTemps = TemperatureReaderUtil.getCityTemperatures(dirName + File.separatorChar + f);
				System.out.println("Reading temperatures from file:"+  f);
				byte[] bytes = SerializeUtil.serialize(cityTemps);
				cityTemps = (CityTemperatures) SerializeUtil.deserialize(bytes);
				cityTempMap.put(f, cityTemps);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (String f: files) {
			CityTemperatures cityTemps = cityTempMap.get(f);
			TreeSet<Integer> temps = cityTemps.get(cityName);
			if (temps != null) {
				System.out.println (cityName + ": " + temps.toString());
			}
		}
	}
}

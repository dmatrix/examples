/**
 * 
 */
package temperatures;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author jules damji
 *
 * CityTemperatues defines the key data structure for the data set. Each instance stores its day, city:state,
 * and set of recorded temperatures. The default state is California; however, it should be
 * extended to hold a state other than the default. Together with the city, it would make a unique key and scale
 * to include all the states with the US.
 * 
 */
public class CityTemperatures implements Serializable{
	
	private static final long serialVersionUID = 1L;
	// map that holds city:state (key)-->[ordered set of temperatures, duplicates are discarded]
	private Map <String, TreeSet<Integer>> mCityMap;
	// default state is CA
	private static String mState = "CA".toLowerCase();
	// the day or name of the file
	private String mKey;
	
	/**
	 *  Constructor
	 * @param pKey primary day of the week (name of the file)
	 */
	public CityTemperatures(String pKey) { 
		mKey = pKey;
		mCityMap = new HashMap<String, TreeSet<Integer>>();
	}
	
	/**
	 * convenience method to construct a unique: stripped, lower case, and appended with the state
	 * @param pKey unique city within a state
	 * @return a "city:state"
	 */
	public static String constructKey(String pKey) {
		StringBuffer b = new StringBuffer();
		char[] chars = pKey.toLowerCase().toCharArray();
		for (char c: chars) {
			if (c == ' ') continue;
			b.append(c);
		}
		b.append(':');
		b.append(mState);
		return b.toString();
	}
	
	/**
	 * Add recorded temperature to an existing data set for a given city
	 * @param pCity City
	 * @param pTemp recoreded temperature for the city
	 */
	public void add(String pCity, int pTemp) {
		String key = pCity;
		if (pCity.indexOf(':') < 0) {
			// construct uniquey key (city:state)
			 key = constructKey(pCity);
		}
		TreeSet<Integer> ts = mCityMap.get(key);
		if (ts == null) {
			ts = new TreeSet<Integer>();
		}
		ts.add(pTemp);
		mCityMap.put(key, ts);
	}
	
	public Set<String> getCities () {
		return mCityMap.keySet();
	}
	
	/**
	 * getter method for the city
	 * @param pCity
	 * @return Set of Recorded temperatures for the city
	 */
	public TreeSet<Integer> get(String pCity) {
		// construct unique key (city:state)
		if (pCity.indexOf(':') < 0) 
			return mCityMap.get(constructKey(pCity));
		else 
			return mCityMap.get(pCity);
		
	}
	
	/**
	 * Getter method for the city
	 * @return city
	 */
	public String getKey() { return mKey; }
}

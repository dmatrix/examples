/**
 * 
 */
package data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author jules
 *
 */
public class CityTemperatures implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map <String, TreeSet<Integer>> mCityMap;
	
	public CityTemperatures() { 
		mCityMap = new HashMap<String, TreeSet<Integer>>();
	}
	
	public void add(String pCity, int pTemp) {
		TreeSet<Integer> ts = mCityMap.get(pCity);
		if (ts == null) {
			ts = new TreeSet<Integer>();
		}
		ts.add(pTemp);
		mCityMap.put(pCity, ts);
	}
	
	public Set<String> getCities () {
		return mCityMap.keySet();
	}
	public TreeSet<Integer> get(String pCity) {
		return mCityMap.get(pCity);
		
	}
}

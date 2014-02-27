/**
 * 
 */
package temperatures;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import util.SerializeUtil;

import com.continuuity.api.annotation.UseDataSet;
import com.continuuity.api.data.dataset.KeyValueTable;

import data.CityTemperatures;

/**
 * @author jules
 *
 */
public class TemperatureReducer extends Reducer<Text, Text, byte[], CityTemperatures> {
	
	@UseDataSet("processedfiletable")
	KeyValueTable processedFileTable;
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		CityTemperatures reducedCityTemps = new CityTemperatures();
		try {
			for (Text cityTemps: values) {
				CityTemperatures ct = (CityTemperatures) SerializeUtil.deserialize(cityTemps.getBytes());
				Set<String> cities = ct.getCities();
					for (String city: cities) {
						TreeSet<Integer> temp = ct.get(city);
						int min = temp.first();
						int max = temp.last();
						reducedCityTemps.add(city, min);
						reducedCityTemps.add(city, max);
					}
				processedFileTable.write(key.getBytes(), SerializeUtil.serialize(reducedCityTemps));
				processedFileTable.close();
				context.write(key.getBytes(), reducedCityTemps);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			processedFileTable.close();
		}
	}
}

/**
 * 
 */
package temperatures;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import com.continuuity.api.annotation.ProcessInput;
import com.continuuity.api.annotation.UseDataSet;
import com.continuuity.api.common.Bytes;
import com.continuuity.api.data.dataset.KeyValueTable;
import com.continuuity.api.flow.flowlet.AbstractFlowlet;

/**
 * @author jules
 *
 */
public class RawTemperatureDataFlowlet extends AbstractFlowlet {

	/**
	 * Use annotation to indicate that what underlying building blocks
	 * this flowlet must access
	 */
	@UseDataSet(TemperaturesApp.PROCESSED_TABLE_NAME)
	KeyValueTable processedFileTable;
	
    @ProcessInput("temperatureData")
	public void processInput(byte[] data) {
    	try {
			CityTemperatures ct = (CityTemperatures) SerializeUtil.deserialize(data);
			CityTemperatures reducedCityTemps = new CityTemperatures(ct.getKey());
			Set<String> cities = ct.getCities();
			for (String city: cities) {
				TreeSet<Integer> temp = ct.get(city);
				int min = temp.first();
				int max = temp.last();
				reducedCityTemps.add(city, min);
				reducedCityTemps.add(city, max);
			}
			processedFileTable.write(Bytes.toBytes(ct.getKey()), SerializeUtil.serialize(reducedCityTemps));
			processedFileTable.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
	}
}

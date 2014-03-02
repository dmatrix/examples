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
 * RawTemperatureDataFlowlet, like the other flowlet, represents one of the nodes in our DAG. Instead of reading
 * data from a file, it reads data from an event stream attached or connected to it. (Note, how we connected this
 * flowlet in the RawFileFlow class. 
 * 
 * Again, for massive scalability and large number of incoming datasets, this flowlet could configured to create multiple instances,
 * each processing a event. Reactor ensures that no dataset is ever lost, meaning that all events in the Event queue are persisted.
 * 
 */
public class RawTemperatureDataFlowlet extends AbstractFlowlet {

	/**
	 * Use annotation to indicate that what underlying building blocks
	 * this flowlet must access. Note, how we specified the connection from the previous flowlet, RawFileFlowlet, to
	 * emit data on to an event queue stream called "temperatureData." Here, we merely indicate to the Runtime engine
	 * that this flowlet is the receiver or consumer of that event.
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
			// store the transformed data with min, max temperature for the data for that city:state.
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

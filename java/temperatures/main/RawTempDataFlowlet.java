/**
 * 
 */
package temperatures.main;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import temperatures.cdata.CityTemperatures;
import temperatures.util.SerializeUtil;

import com.continuuity.api.annotation.ProcessInput;
import com.continuuity.api.annotation.UseDataSet;
import com.continuuity.api.common.Bytes;
import com.continuuity.api.data.dataset.KeyValueTable;
import com.continuuity.api.flow.flowlet.Flowlet;
import com.continuuity.api.flow.flowlet.FlowletContext;
import com.continuuity.api.flow.flowlet.FlowletSpecification;
import com.continuuity.api.flow.flowlet.StreamEvent;

/**
 * @author jules
 *
 */
public class RawTempDataFlowlet implements Flowlet {

	/* (non-Javadoc)
	 * @see com.continuuity.api.flow.flowlet.Flowlet#configure()
	 */
	public FlowletSpecification configure() {
		// TODO Auto-generated method stub
		return null;
	}
	@UseDataSet("processedfiletable")
	KeyValueTable processedFileTable;
	
    @ProcessInput
	public void processInput(StreamEvent event) {
    	byte [] data = Bytes.toBytes(event.getBody());
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
	/* (non-Javadoc)
	 * @see com.continuuity.api.flow.flowlet.Flowlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.continuuity.api.flow.flowlet.Flowlet#initialize(com.continuuity.api.flow.flowlet.FlowletContext)
	 */
	public void initialize(FlowletContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}

/**
 * 
 */
package temperatures;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.continuuity.api.annotation.Output;
import com.continuuity.api.annotation.ProcessInput;
import com.continuuity.api.annotation.UseDataSet;
import com.continuuity.api.common.Bytes;
import com.continuuity.api.data.dataset.KeyValueTable;
import com.continuuity.api.flow.flowlet.AbstractFlowlet;
import com.continuuity.api.flow.flowlet.OutputEmitter;
import com.continuuity.api.flow.flowlet.StreamEvent;

/**
 * @author jules damji
 * 
 * RawFileFlowlet represents one of the nodes in our DAG, in which data is read from one of the files,
 * transformed into a raw data set, and then passed on to the connected flowlet for further transformation.
 * 
 * For scalability and large number of directories, this flowlet could configured to create multiple instances
 * to that each directory processes its own set of files, since each directory has a unique path, and within the
 * directory unique files, with its cities recorded temperatures.
 *
 */
public class RawFileFlowlet extends AbstractFlowlet {

	/**
	 * Use annotation to indicate that what underlying building blocks
	 * this flowlet must access. For example, here we access a KeyValueTable DataSet for storing
	 * our raw recorded temperatures. Other runtime indicators are that this flowlet will emit
	 * data as byte[] to the emitter "outputdata," which is connected to another flowlet.
	 */
	@UseDataSet(TemperaturesApp.RAW_TABLE_NAME)
	KeyValueTable rawFileTable;
	@Output("temperatureData")
	OutputEmitter<byte[]> outputdata;
    @ProcessInput
	public void processInput(StreamEvent event) {
    	String dirname;
		byte [] dir = Bytes.toBytes(event.getBody());
		if (dir != null && dir.length > 0) {
			dirname = Bytes.toString(dir);
		} else {
			dirname = "/Users/jules/gitexamples/examples/java/data";
		}
		List<String> listFiles = FileUtils.listOfFiles(dirname);
		for (String f: listFiles) {
			try {
				CityTemperatures cityTemperatures = TemperatureReaderUtil.getCityTemperatures(dirname + File.separatorChar + f);
				if (cityTemperatures != null) {
					byte[] data = SerializeUtil.serialize(cityTemperatures);
					byte[] key = Bytes.toBytes(f);
					// write to the table
					rawFileTable.write(key, data);
					//write to the connecting stream; this will generate an event for the
					//consuming flowlet to absorb the event, process, transform, and store it
					// into the table for procedures to consume and respond to external queiries
					//
					outputdata.emit(data);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

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
 * transformed into a raw data set, stored into a key/value table, and then passed on to the connected flowlet 
 * for further transformation.
 * 
 * For a large number of directories, this flowlet could be configured to create multiple instances to handle the load.
 *
 */
public class RawFileFlowlet extends AbstractFlowlet {

	/**
	 * I use annotations to indicate what underlying building blocks this flowlet must access. For example, 
	 * here I access a KeyValueTable Dataset for storing our raw recorded temperatures. Another runtime 
	 * annotation indicator is @Output, indicating that this flowlet will emit
	 * data as byte[] to the emitter "outputdata," which is connected to another flowlet.
	 * UseDataSet - indicates this flowlet will access the KeyValueTable defined in the main appplication file.
	 * Output     - indicates this flowlet will emit or write data to an output stream.
	 * ProcessInput - indicates the method that will handle the Stream Events on the Stream connected to this flowlet
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
					// write to the key/value table
					rawFileTable.write(key, data);
					//write to the connecting stream; this will generate an event for the
					//consuming flowlet to absorb the event, process, transform, and store it
					// into the table for procedures to consume and respond to external queries
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

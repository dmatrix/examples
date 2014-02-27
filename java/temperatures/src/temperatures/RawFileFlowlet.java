/**
 * 
 */
package temperatures;

import java.io.File;
import java.io.IOException;
import java.util.List;

import util.FileUtils;
import util.SerializeUtil;
import util.TemperatureReaderUtil;

import com.continuuity.api.annotation.ProcessInput;
import com.continuuity.api.annotation.UseDataSet;
import com.continuuity.api.common.Bytes;
import com.continuuity.api.data.dataset.KeyValueTable;
import com.continuuity.api.flow.flowlet.AbstractFlowlet;
import com.continuuity.api.flow.flowlet.StreamEvent;

import data.CityTemperatures;

/**
 * @author jules
 *
 */
public class RawFileFlowlet extends AbstractFlowlet {

	public final static String RAW_PREFIX = "raw-";
	/**
	 * 
	 */
	@UseDataSet("rawfiletable")
	KeyValueTable rawFileTable;
	
	public RawFileFlowlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public RawFileFlowlet(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
    @ProcessInput
	
	public void processInput(StreamEvent event) {
    	String dirname;
		byte [] dir = Bytes.toBytes(event.getBody());
		if (dir != null && dir.length > 0) {
			dirname = Bytes.toString(dir);
		} else {
			dirname = "/Users/jules/src/java/continuuity/src/temperatures/data";
		}
		List<String> listFiles = FileUtils.listOfFiles(dirname);
		for (String f: listFiles) {
			try {
				CityTemperatures cityTemperatures = TemperatureReaderUtil.getCityTemperatures(dirname + File.separatorChar + f);
				if (cityTemperatures != null) {
					byte[] data = SerializeUtil.serialize(cityTemperatures);
					byte[] key = Bytes.toBytes(RAW_PREFIX + f);
					rawFileTable.write(key, data);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

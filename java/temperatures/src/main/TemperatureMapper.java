/**
 * 
 */
package temperatures;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.SerializeUtil;

import com.continuuity.api.annotation.UseDataSet;
import com.continuuity.api.data.batch.Split;
import com.continuuity.api.data.dataset.KeyValueTable;

import data.CityTemperatures;

/**
 * @author jules damji
 *
 */
public class TemperatureMapper extends Mapper<byte[], CityTemperatures, Text, CityTemperatures> {
	
	private static Logger LOG = LoggerFactory.getLogger(TemperatureMapper.class);
	@UseDataSet("rawfiletable")
	KeyValueTable rawFileTable;
	
	public void map(byte[] key, CityTemperatures value, Context context) throws IOException, InterruptedException {
		LOG.debug("Temperature Mapper: Did I even get here... ?");
		List<Split> splits = rawFileTable.getSplits();
		for (Split split: splits) {
			KeyValueTable.KeyValueScanner keyScanner = rawFileTable.new KeyValueScanner(split);
			while (keyScanner.nextKeyValue()) {
				byte[] k = keyScanner.getCurrentKey();
				CityTemperatures v;
				try {
					v = (CityTemperatures) SerializeUtil.deserialize(keyScanner.getCurrentValue());
					context.write(new Text(k), v);
				} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			keyScanner.close();
		}
		rawFileTable.close();
	}
}
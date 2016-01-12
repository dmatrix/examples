/**
 * 
 */
package computesum;

import com.continuuity.api.annotation.ProcessInput;
import com.continuuity.api.annotation.UseDataSet;
import com.continuuity.api.common.Bytes;
import com.continuuity.api.data.dataset.KeyValueTable;
import com.continuuity.api.flow.flowlet.AbstractFlowlet;
import com.continuuity.api.flow.flowlet.StreamEvent;

/**
 * @author jules
 *
 */
public class NumberSaver extends AbstractFlowlet {
	
	
	static final int[] DATA = { 82, 105, 99, 104, 97, 114, 100, 32, 72, 121, 100, 101, 33, 85, 76, 83, 1239, 10, 19 };
	
	public static byte[] M_KEY= Bytes.toBytes("messages");
	public static byte[] W_KEY= Bytes.toBytes("wages");

	
	@UseDataSet("numTable")
	KeyValueTable numTable;

	/**
	 * 
	 */
	public NumberSaver() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public NumberSaver(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@ProcessInput
	
	public void processInput(StreamEvent event) {
		byte [] messages = Bytes.toBytes(event.getBody());
		if (messages != null && messages.length > 0) {
			numTable.write(M_KEY, messages);
		}
		int sum = 0;
		for (int i = 0; i < DATA.length; i++) {
			sum = sum + DATA[i];
		}
		numTable.write(W_KEY, Bytes.toBytes(sum));

	}
}

/**
 * 
 */
package temperatures;

import com.continuuity.api.flow.Flow;
import com.continuuity.api.flow.FlowSpecification;
/**
 * @author jules
 *
 */
public class RawFileFlow implements Flow {

	/* (non-Javadoc)
	 * @see com.continuuity.api.flow.Flow#configure()
	 */
	@Override
	public FlowSpecification configure() {
		// TODO Auto-generated method stub
		return FlowSpecification.Builder.with()
		.setName("RawFileFlow")
		.setDescription("A flow that collects temperatures")
		.withFlowlets()
		 	.add("file_flowlet", new RawFileFlowlet())
		 	.add("data_flowlet", new RawTemperatureDataFlowlet())
		.connect()
			.fromStream("datadirectory").to("file_flowlet")
			.from("file_flowlet").to("data_flowlet")
		.build();
	}
}

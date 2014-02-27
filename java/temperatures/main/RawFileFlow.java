/**
 * 
 */
package temperatures.main;

import com.continuuity.api.flow.Flow;
import com.continuuity.api.flow.FlowSpecification;
/**
 * @author jules
 *
 */
public class RawFileFlow implements Flow {

	/**
	 * 
	 */
	public RawFileFlow() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.continuuity.api.flow.Flow#configure()
	 */
	public FlowSpecification configure() {
		// TODO Auto-generated method stub
		return FlowSpecification.Builder.with()
		.setName("RawFileFlow")
		.setDescription("A flow that collects numbers")
		.withFlowlets().add("rawfileflowset", new RawFileFlowlet())
		.add("rawTempdataflowset", new RawTempDataFlowlet())
		.connect().fromStream("datadirectory").to("rawfileflowset")
		.from("tempsData").to("rawTempdataflowset")
		.build();
	}
}

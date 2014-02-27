/**
 * 
 */
package computesum;

import com.continuuity.api.flow.Flow;
import com.continuuity.api.flow.FlowSpecification;

/**
 * @author jules
 *
 */
public class AddFlow implements Flow {

	/**
	 * 
	 */
	public AddFlow() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.continuuity.api.flow.Flow#configure()
	 */
	@Override
	public FlowSpecification configure() {
		// TODO Auto-generated method stub
		return FlowSpecification.Builder.with()
			.setName("AddFlow")
			.setDescription("A flow that collects numbers")
			.withFlowlets().add("numsaver", new NumberSaver())
			.connect().fromStream("numbers").to("numsaver")
			.build();
	}

}

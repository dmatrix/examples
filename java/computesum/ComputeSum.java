/**
 * 
 */
package computesum;


import com.continuuity.api.Application;
import com.continuuity.api.ApplicationSpecification;
import com.continuuity.api.data.dataset.KeyValueTable;
import com.continuuity.api.data.stream.Stream;

/**
 * @author jules
 *
 */
public class ComputeSum implements Application {

	/**
	 * 
	 */
	public ComputeSum() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.continuuity.api.Application#configure()
	 */
	@Override
	public ApplicationSpecification configure() {
		// TODO Auto-generated method stub
		return ApplicationSpecification.Builder.with()
			.setName("ComputeSum")
			.setDescription("Reads a stream of numbers and computers sum")
			.withStreams().add(new Stream("numbers"))
			.withDataSets().add(new KeyValueTable("numTable"))
			.withFlows().add(new AddFlow())
			.withProcedures().add(new SumProcedure())
			.noMapReduce()
			.noWorkflow()
			.build();
			
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

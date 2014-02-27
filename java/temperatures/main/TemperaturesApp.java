/**
 * 
 */
package temperatures.main;

import com.continuuity.api.Application;
import com.continuuity.api.ApplicationSpecification;
import com.continuuity.api.data.dataset.KeyValueTable;
import com.continuuity.api.data.stream.Stream;
/**
 * @author jules
 *
 */
public class TemperaturesApp implements Application {
	/**
	 * 
	 */
	public TemperaturesApp() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.continuuity.api.Application#configure()
	 */
	public ApplicationSpecification configure() {
		// TODO Auto-generated method stub
		return ApplicationSpecification.Builder.with()
		.setName("TemperaturesApp")
		.setDescription("Reads a set of files with city temperatures and store for batch processing")
		.withStreams().add(new Stream("datadirectory"))
		.withDataSets().add(new KeyValueTable("rawfiletable"))
		.add(new KeyValueTable("processedfiletable"))
		.withFlows().add(new RawFileFlow())
		.noProcedure()
		.noMapReduce()
		.noWorkflow()
		.build();
	}
		
}

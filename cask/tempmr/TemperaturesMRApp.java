/**
 * 
 */
package tempmr;

import com.continuuity.api.Application;
import com.continuuity.api.ApplicationSpecification;
import com.continuuity.api.data.dataset.KeyValueTable;
import com.continuuity.api.data.stream.Stream;
/**
 * @author jules damji
 *
 */
public class TemperaturesMRApp implements Application {
	/**
	 * The main application that glues all the Reactor components together. Built as a jar file,
	 * the jar file is deployed via the gateway into the application fabric that instantiates, 
	 * allocates resources, manages its execution, and delegates its distribution.
	 */
	  public static final String RAW_TABLE_NAME = "rawfiletable";
	  public static final String PROCESSED_TABLE_NAME = "processedfiletable";
	  public static final String DATA_DIRECTORY_STREAM = "datadirectory";

	/* (non-Javadoc)
	 * @see com.continuuity.api.Application#configure()
	 */
	@Override
	public ApplicationSpecification configure() {
		// TODO Auto-generated method stub
		return ApplicationSpecification.Builder.with()
		.setName("TemperaturesMRApp")
		.setDescription("Read dataset of city temperatures stored for batch processing")
		.noStream()
		.withDataSets()
			.add(new KeyValueTable(RAW_TABLE_NAME))
			.add(new KeyValueTable(PROCESSED_TABLE_NAME))
		.noFlow()
		.withProcedures()
			.add(new TemperatureMRProcedure())
		.withMapReduce()
			.add(new TemperatureMapReducer())
		.noWorkflow()
		.build();
	}
		
}

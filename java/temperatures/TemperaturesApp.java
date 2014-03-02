/**
 * 
 */
package temperatures;

import com.continuuity.api.Application;
import com.continuuity.api.ApplicationSpecification;
import com.continuuity.api.data.dataset.KeyValueTable;
import com.continuuity.api.data.stream.Stream;
/**
 * @author jules damji
 *
 */
public class TemperaturesApp implements Application {
	/**
	 * The main application that glues all the Reactor components together. Built as a jar file,
	 * the jar file is deployed via the gateway into the application fabric that instantiates, 
	 * allocates resources, manages its execution, and delegates its distribution.
	 */
	  public static final String RAW_TABLE_NAME = "rawfiletable";
	  public static final String PROCESSED_TABLE_NAME = "processedfiletable";
	  public static final String DATA_DIRECTORY_STREAM = "datadirectory";

	public TemperaturesApp() {
		// TODO Auto-generated constructor stub
	}
	
	 public static void main(String[] args) {
		    // Main method should be defined for Application to get deployed with Eclipse IDE plugin. DO NOT REMOVE IT
		  }

	/* (non-Javadoc)
	 * @see com.continuuity.api.Application#configure()
	 */
	@Override
	public ApplicationSpecification configure() {
		// TODO Auto-generated method stub
		return ApplicationSpecification.Builder.with()
		.setName("TemperaturesApp")
		.setDescription("Reads a set of files with city temperatures and store for batch processing")
		.withStreams().
			add(new Stream(DATA_DIRECTORY_STREAM))
		.withDataSets()
			.add(new KeyValueTable(RAW_TABLE_NAME))
			.add(new KeyValueTable(PROCESSED_TABLE_NAME))
		.withFlows()
			.add(new RawFileFlow())
		.withProcedures()
			.add(new TemperatureProcedure())
		.noMapReduce()
		.noWorkflow()
		.build();
	}
		
}

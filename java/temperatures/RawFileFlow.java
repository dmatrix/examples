/**
 * 
 */
package temperatures;

import com.continuuity.api.flow.Flow;
import com.continuuity.api.flow.FlowSpecification;
/**
 * @author jules damji
 * 
 * A flow is a transformation mapping of how you want your data to traverse. It's represented
 * as a directed acyclic graph (DAG), in which each node represents a transformation point, where
 * ingested data are either changed and then stored or transfered to the next node along its directed
 * graph. The nodes, then, are entities of change, and in the parlance of Continuuity Reactor, they are 
 * called Flowlets.
 * 
 * The class represents our our Temperature flow, with two flowlets: the
 * RawFileFlowlet (which reads temperatures from data files, convertes them into dataset, stores dataset 
 * into a key/value table, and passes the raw dataset to the next flowlet) and the RawTemperatureDataFlowlet
 * (which receives raw dataset, further transforms the data, and stores it into another key/value table).
 * 
 * In this flow class, we configure our flow by a) adding flowlets b) connecting relevant flowlets
 * to a data stream, and c) connect flowlets with each other so that they can pass data from the 
 * sender flowlet to the receiver flowlet.
 * 
 * As you may notice, the Flow idea is simple to conceive, easy to configure, and intuitive to read.
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
			//even though we have not created an explicit stream here to connect to the flowlet
			//there is data being written or emitted from one flowlet to another flowlet via a stream
			//as events, which are queued. All events are persisted, so they're guranteed for delivery.
			.from("file_flowlet").to("data_flowlet")
		.build();
	}
}

/**
 * 
 */
package tempmr;


import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import temperatures.CityTemperatures;

import com.continuuity.api.mapreduce.MapReduce;
import com.continuuity.api.mapreduce.MapReduceContext;
import com.continuuity.api.mapreduce.MapReduceSpecification;

/**
 * @author jules damji
 *
 */
public class TemperatureMapReducer implements MapReduce {
	
	public MapReduceSpecification configure() {
		// TODO Auto-generated method stub
		return MapReduceSpecification.Builder.with()
		.setName("TemperatureMapReducer")
		.setDescription("Sorts Max and Min temperatures for each city")
		.useInputDataSet("rawfiletable") 
		.useOutputDataSet("processedfiletable") 
		.build();
	}

	
	/* (non-Javadoc)
	 * @see com.continuuity.api.mapreduce.MapReduce#beforeSubmit(com.continuuity.api.mapreduce.MapReduceContext)
	 */
	
	public void beforeSubmit(MapReduceContext pContext) throws Exception {
		Job job = pContext.getHadoopJob();
		
		job.setMapperClass(TemperatureMapper.class);
		job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(CityTemperatures.class);
		job.setReducerClass(TemperatureReducer.class);
	}

	/* (non-Javadoc)
	 * @see com.continuuity.api.mapreduce.MapReduce#configure()
	 */


	/* (non-Javadoc)
	 * @see com.continuuity.api.mapreduce.MapReduce#onFinish(boolean, com.continuuity.api.mapreduce.MapReduceContext)
	 */
	public void onFinish(boolean arg0, MapReduceContext arg1) throws Exception {
		// TODO Auto-generated method stub

	}

}

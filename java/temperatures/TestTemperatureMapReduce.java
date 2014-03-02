/**
 * 
 */
package temperatures;

import java.util.concurrent.TimeUnit;

import com.continuuity.api.Application;
import com.continuuity.test.ApplicationManager;
import com.continuuity.test.MapReduceManager;
import com.continuuity.test.ReactorTestBase;

/**
 * @author jules damji
 *
 */
public class TestTemperatureMapReduce extends ReactorTestBase {

	/**
	 * 
	 */
	public TestTemperatureMapReduce() {
		try {
			testApp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testApp() throws Exception {
		ApplicationManager applicationManager = deployApplication((Class<? extends Application>) TemperaturesApp.class);
		
		try {
		      MapReduceManager mrManager = applicationManager.startMapReduce("TemperatureMapReducer");
		      mrManager.waitForFinish(120L, TimeUnit.SECONDS);
		      
		    } finally {
		      applicationManager.stopAll();
		      TimeUnit.SECONDS.sleep(1);
		      clear();
		}
	}
	
	public static void main (String args[]) {
		TestTemperatureMapReduce testMapReduce = new TestTemperatureMapReduce();
		try {
			testMapReduce.testApp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

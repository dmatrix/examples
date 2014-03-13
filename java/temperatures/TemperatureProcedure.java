/**
 * 
 */
package temperatures;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.TreeSet;

import com.continuuity.api.annotation.Handle;
import com.continuuity.api.annotation.UseDataSet;
import com.continuuity.api.common.Bytes;
import com.continuuity.api.data.dataset.KeyValueTable;
import com.continuuity.api.procedure.AbstractProcedure;
import com.continuuity.api.procedure.ProcedureRequest;
import com.continuuity.api.procedure.ProcedureResponder;
import com.continuuity.api.procedure.ProcedureResponse;
import com.continuuity.api.procedure.ProcedureResponse.Code;

/**
 * @author jules damji
 *
 * TemperatureProcedure class represents one of the core building block in the Reactor Continuuity paradigm. That is,
 * they facilitate and satisfy external queries to the transformed and stored data sets.
 */
public class TemperatureProcedure extends AbstractProcedure {
	
	/**
	 * Use processed table DataSet, and handle request
	 */
	@UseDataSet(TemperaturesApp.PROCESSED_TABLE_NAME)
	KeyValueTable processedFileTable;
	/**
	 * Handler method that will satisfy the request.
	 * @param request Instance from which request parameters can be extracted
	 * @param responder Instance to which response from a queried data set can be conveyed.
	 */
	@Handle("getTemperature")
	public void getTemperature(ProcedureRequest request, ProcedureResponder responder) {
	    String day = request.getArgument("day");
	    String city = request.getArgument("city");
	    if (day == null || city == null) {
	      try {
			responder.error(Code.CLIENT_ERROR,
			                  "Method 'getTemperature' requires argument two arguments: 'day and city'");
	      } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	      }
	    } else {
	    	byte[] dayKey = Bytes.toBytes(day.toLowerCase().trim());
	    	byte[] data = processedFileTable.read(dayKey);
			try {
				CityTemperatures ct = (CityTemperatures) SerializeUtil.deserialize(data);
				if (ct != null) {
					TreeSet<Integer> temp = ct.get(city);
					if (temp != null) {
						int min = temp.first();
						int max = temp.last();
						String reply = String.format("[Day=%s; City=%s; Max=%d; Min=%d", day, city, max, min);
						ProcedureResponse.Writer writer =
							      responder.stream(new ProcedureResponse(Code.SUCCESS));
							    writer.write(ByteBuffer.wrap(reply.getBytes())).close();
					}
				} else {
					responder.error(Code.CLIENT_ERROR,
							String.format("Method 'getTemperature' failed or couldn't find data for : %s and %s", day, city));
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
}

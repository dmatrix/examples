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
 * @author jules
 *
 */
public class TemperatureProcedure extends AbstractProcedure {
	
	/**
	 * Use my processed table DataSet, and handle request
	 */
	@UseDataSet(TemperaturesApp.PROCESSED_TABLE_NAME)
	KeyValueTable processedFileTable;
	/**
	 * Handler method 
	 * @param request
	 * @param responder
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
	    	byte[] dayKey = Bytes.toBytes(day);
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
	    return;
	}
}

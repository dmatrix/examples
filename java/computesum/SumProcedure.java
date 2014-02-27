/**
 * 
 */
package computesum;


import com.continuuity.api.annotation.Handle;
import com.continuuity.api.annotation.UseDataSet;
import com.continuuity.api.common.Bytes;
import com.continuuity.api.data.dataset.KeyValueTable;
import com.continuuity.api.procedure.AbstractProcedure;
import com.continuuity.api.procedure.ProcedureRequest;
import com.continuuity.api.procedure.ProcedureResponder;


/**
 * @author jules
 *
 */
public class SumProcedure extends AbstractProcedure {

	/**
	 * 
	 */
	public SumProcedure() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public SumProcedure(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@UseDataSet("numTable")
	KeyValueTable numTable;
	
	@Handle("summation")
	public void summation(ProcedureRequest request, ProcedureResponder responder) throws Exception {
		byte[] sumBytes = numTable.read(NumberSaver.W_KEY);
		
		int sum = Bytes.toInt(sumBytes);
		
		byte[] messages = numTable.read(NumberSaver.M_KEY);
		String msg = messages != null ? new String(messages) : "None Found"; 
		
		responder.sendJson("Messages: " + msg + "; Wages: " + sum);
	}
	
}

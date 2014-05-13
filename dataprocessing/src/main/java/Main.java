import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.hadoop.HadoopFlowConnector;
import cascading.operation.aggregator.Count;
import cascading.operation.filter.Sample;
import cascading.operation.filter.Limit;
import cascading.operation.regex.RegexParser;
import cascading.operation.text.DateParser;
import cascading.pipe.*;
import cascading.property.AppProps;
import cascading.scheme.hadoop.TextDelimited;
import cascading.scheme.hadoop.TextLine;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

    	// input (taps) and output (sinks)
        String inputPath	= args[0];
        String outputPath 	= args[1];
        // sources and sinks
        Tap inTap 	= new Hfs(new TextLine(), inputPath);
        Tap outTap  = new Hfs(new TextDelimited(true, "\t"), outputPath, SinkMode.REPLACE);
        // Parse the line of input and break them into five fields
        RegexParser parser = new RegexParser(new Fields("ip", "time", "request", "response", "size"), 
        		"^([^ ]*) \\S+ \\S+ \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) ([^ ]*).*$", new int[]{1, 2, 3, 4, 5});
        // Create a pipe for processing each line at a time
        Pipe processPipe = new Each("processPipe", new Fields("line"), parser, Fields.RESULTS);
        // Group the stream within the pipe by the field "ip"
        processPipe = new GroupBy(processPipe, new Fields("ip"));
        // Aggregate each "ip" group using the Cascading built in Count function
        processPipe = new Every(processPipe, Fields.GROUP, new Count(new Fields("IPcount")), Fields.ALL);
        // After agreegation counter for each "ip," sort the counts
        Pipe sortedCountByIpPipe = new GroupBy(processPipe, new Fields("IPcount"), true);
        // Limit them to the first 10, in the desceding order
        sortedCountByIpPipe = new Each(sortedCountByIpPipe, new Fields("IPcount"), new Limit(10));
        // Join the pipe together in the flow, creating inputs and outputs (taps)
        FlowDef flowDef = FlowDef.flowDef()
    		   .addSource(processPipe, inTap)
    		   .addTailSink(sortedCountByIpPipe, outTap)
    		   .setName("DataProcessing");
        Properties properties = AppProps.appProps()
        		.setName("DataProcessing")
        		.buildProperties();
        Flow parsedLogFlow = new HadoopFlowConnector(properties).connect(flowDef);
        //Finally, execute the flow.
        parsedLogFlow.complete();
    }
}

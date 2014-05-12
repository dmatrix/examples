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

    	 // arguments from the command line. HDFS paths to the input directory containing logs and
    	 // output directory where we going to store the results from the run.
        String inputPath = args[0];
        String outputPath = args[1];

        // Create the High level logical constructs Taps (Sources and Sinks)
        //
        Tap inTap =   new Hfs(new TextLine(), inputPath);
        Tap outTap = new Hfs(new TextDelimited(true, "\t"), outputPath, SinkMode.REPLACE);
        
        // define the fields to parse
        Fields apacheFields = new Fields("ip", "time", "request", "response", "size");
        
        // define a regex operation to split the "document" text lines into a token stream
        String apacheRegex = "^([^ ]*) \\S+ \\S+ \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) ([^ ]*).*$";
        
        // define the five groups for our splits
        int[] allGroups = {1, 2, 3, 4, 5};
        RegexParser parser = new RegexParser(apacheFields, apacheRegex, allGroups);
        // create a pipe for each line at a time
        Pipe regexImport = new Each("regexImport", new Fields("line"), parser, Fields.RESULTS);
        // use the date parser to parse the "date" field in the log on each line
        DateParser dateParser = new DateParser(new Fields("time"), "dd/MMM/yyyy:HH:mm:ss Z");
        // another pipe for the time tuple
        Pipe transformPipe = new Each(regexImport, new Fields("time"), dateParser, Fields.REPLACE);
        // create a pipes for aggregation, grouping, and sorting
        Pipe countByIpPipe = new Pipe("countByIpPipe", transformPipe);
        countByIpPipe = new GroupBy(countByIpPipe, new Fields("ip"));
        countByIpPipe = new Every(countByIpPipe, Fields.GROUP, new Count(new Fields("IPcount")), Fields.ALL);
        Pipe sortedCountByIpPipe = new GroupBy(countByIpPipe, new Fields("IPcount"), true);
        //we only want the top 10 counts
        sortedCountByIpPipe = new Each(sortedCountByIpPipe, new Fields("IPcount"), new Limit(10));
        //define a flow where pipes are joined together.
        FlowDef flowDef = FlowDef.flowDef()
        		.addSource(transformPipe, inTap)
        		.addTailSink(sortedCountByIpPipe, outTap)
        		.setName("DataProcessing");
        // build the flow properties
        Properties properties = AppProps.appProps()
        		.setName("DataProcessing")
        		.buildProperties();
        Flow parsedLogFlow = new HadoopFlowConnector(properties).connect(flowDef);
        // execute the flow
        parsedLogFlow.complete();
    }
}

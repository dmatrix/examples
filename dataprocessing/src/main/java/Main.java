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

public class
        Main {
    public static void main(String[] args) {

        String inputPath = args[0];
        String outputPath = args[1];
        Tap inTap = new Hfs(new TextLine(), inputPath);
        Tap outTap = new Hfs(new TextDelimited(true, "\t"), outputPath, SinkMode.REPLACE);

RegexParser parser = new RegexParser(new Fields("ip", "time", "request", "response", "size"), "^([^ ]*) \\S+ \\S+ \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) ([^ ]*).*$", new int[]{1, 2, 3, 4, 5});
Pipe processPipe = new Each("processPipe", new Fields("line"), parser, Fields.RESULTS);
       processPipe = new GroupBy(processPipe, new Fields("ip"));
       processPipe = new Every(processPipe, Fields.GROUP, new Count(new Fields("IPcount")), Fields.ALL);
Pipe sortedCountByIpPipe = new GroupBy(processPipe, new Fields("IPcount"), true);
sortedCountByIpPipe = new Each(sortedCountByIpPipe, new Fields("IPcount"), new Limit(10));
FlowDef flowDef = FlowDef.flowDef()
        .addSource(processPipe, inTap)
        .addTailSink(sortedCountByIpPipe, outTap)
        .setName("DataProcessing");

        Properties properties = AppProps.appProps()
        .setName("DataProcessing")
        .buildProperties();
        Flow parsedLogFlow = new HadoopFlowConnector(properties).connect(flowDef);

        parsedLogFlow.complete();
    }
}

package commandline;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

/**
 * @author lorabit
 * @since 16-3-17
 */
public class Common {

  public static void main(String[] args) throws ParseException {
    Options options = new Options();
    options.addOption("zkcomm", true, "zk comm endpoint");
    options.addOption("zkkafka", true, "zk kafka endpoint");
    options.addOption("biz", true, "biz name");
    options.addOption("port", true, "console port");
    options.addOption("hdfs", true, "hdfs endpoint");
    options.addOption("parallel", true, "parallel thread count");
    options.addOption("cluster", true, "kafka cluster name");
    options.addOption("topics", true, "topics");
    options.addOption("daily", false, "daily flush data to hdfs (hourly by default)");
    CommandLineParser parser = new PosixParser();
    CommandLine cmd = parser.parse( options, args);
    String s = cmd.getOptionValue("zkcomm");
    System.out.println(s);
  }


  @Test
  public void testInput(){

  }
}

//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
// Distributed under the terms of the Modified BSD License.
//

package at.ac.ait.lablink.clients.dpbridge;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class DatapointBridgeClient {

  /** The Constant DEFAULT_CONFIG_FILE. */
  private static final String DEFAULT_CONFIG_FILE = "/resources/dpbconfig.json";

  /** The Constant DEFAULT_JSON_CONFIG_FILE_URL. */
  private static final String DEFAULT_JSON_CONFIG_FILE_URL =
      "file:///" + System.getProperty("user.dir") + DEFAULT_CONFIG_FILE;

  /** The Constant USAGE. */
  private static final String USAGE = 
      "[-h -c <url> -t <json | python>] -p]";

  /** The Constant HEADER. */
  private static final String HEADER = Utility.INFO_PRODUCT + " [" + Utility.VERSION + "]";

  /** The Constant FOOTER. */
  private static final String FOOTER = 
      "Visit " + Utility.INFO_WEBSITE + " for more information.\n";

  public DatapointBridgeClient() {

  }

  private static void printUsage(Options options) {
    HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.setWidth(80);
    helpFormatter.printHelp(USAGE, HEADER, options, FOOTER);
  }

  /**
   * The main method.
   *
   * @param args the arguments
   * @throws Exception the exception
   */
  public static void main(String[] args) throws Exception {

    Options cliOptions = new Options();
    CommandLineParser parser = new BasicParser();

    cliOptions.addOption("h", "help", false,
        "print usage information");
    cliOptions.addOption("c", "config", true,
        "URL to configuration file (" + DEFAULT_JSON_CONFIG_FILE_URL + ")");
    cliOptions.addOption("t", "type", true,
        "Type of config (JSON or Python script)");
    cliOptions.addOption("p", "print-config", false,
        "Print the configuration to PNG file.");

    String config = DEFAULT_JSON_CONFIG_FILE_URL;
    String type = "json";

    CommandLine commandLine = parser.parse(cliOptions, args);

    if (commandLine.hasOption("c")) {
      config = commandLine.getOptionValue("c");
    }

    if (commandLine.hasOption("t")) {
      type = commandLine.getOptionValue("t");
    }

    if (commandLine.hasOption("h")) {
      printUsage(cliOptions);
      System.exit(0);
    }

    // Create DPB instance.
    DatapointBridge dpb = new DatapointBridge(config, type);

    // Print the name and version
    System.err.println(dpb.getIdentity());

    // Initialize the simulator
    System.err.println("Initializing client...");
    dpb.initialize();

    // Print configuration
    if (commandLine.hasOption("p")) {
      System.err.println(dpb.printConfiguration());
    }
    
    // Print copyrights text.
    System.out.println(Utility.INFO_COPYRIGHTS_TEXT);

    // Start simulator
    System.err.println("Starting client...");
    dpb.start();

    System.err.println("Runing for 100 sec.");
    // Run for 100 seconds
    try {
      Thread.sleep(1000000L);
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }

    System.err.println("Stopping client...");
    dpb.stop();
    System.err.println("Done.");
  }
}

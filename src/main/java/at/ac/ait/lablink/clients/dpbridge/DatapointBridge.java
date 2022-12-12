//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
// Distributed under the terms of the Modified BSD License.
//

package at.ac.ait.lablink.clients.dpbridge;

import at.ac.ait.lablink.clients.dpbridge.Version;
import at.ac.ait.lablink.core.connection.ILlConnection;
import at.ac.ait.lablink.core.connection.impl.LlConnectionFactory;
import at.ac.ait.lablink.core.service.datapoint.consumer.EDataPointConsumerState;
import at.ac.ait.lablink.core.service.datapoint.consumer.IDataPointConsumerService;
import at.ac.ait.lablink.core.service.datapoint.consumer.impl.DataPointConsumerServiceImpl;
import at.ac.ait.lablink.core.service.sync.consumer.impl.SyncClientServiceImpl;

import net.sourceforge.plantuml.SourceStringReader;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.sql.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * Class DatapointBridge.
 */
public class DatapointBridge {

  /** The Constant ANSI_RESET. */
  public static final String ANSI_RESET = "\u001B[0m";

  /** The Constant ANSI_BLACK. */
  public static final String ANSI_BLACK = "\u001B[30m";

  /** The Constant ANSI_RED. */
  public static final String ANSI_RED = "\u001B[31m";

  /** The Constant ANSI_GREEN. */
  public static final String ANSI_GREEN = "\u001B[32m";

  /** The Constant ANSI_YELLOW. */
  public static final String ANSI_YELLOW = "\u001B[33m";

  /** The Constant ANSI_BLUE. */
  public static final String ANSI_BLUE = "\u001B[34m";

  /** The Constant ANSI_PURPLE. */
  public static final String ANSI_PURPLE = "\u001B[35m";

  /** The Constant ANSI_CYAN. */
  public static final String ANSI_CYAN = "\u001B[36m";

  /** The Constant ANSI_WHITE. */
  public static final String ANSI_WHITE = "\u001B[37m";

  /** The Constant ANSI_BLACK_BACKGROUND. */
  public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";

  /** The Constant ANSI_RED_BACKGROUND. */
  public static final String ANSI_RED_BACKGROUND = "\u001B[41m";

  /** The Constant ANSI_GREEN_BACKGROUND. */
  public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

  /** The Constant ANSI_YELLOW_BACKGROUND. */
  public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

  /** The Constant ANSI_BLUE_BACKGROUND. */
  public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";

  /** The Constant ANSI_PURPLE_BACKGROUND. */
  public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

  /** The Constant ANSI_CYAN_BACKGROUND. */
  public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";

  /** The Constant ANSI_WHITE_BACKGROUND. */
  public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

  /** The group name. */
  private String groupName;

  /** The client name. */
  private String clientName;

  /** The scenario name. */
  private String scenarioName;

  /** The url ll properties. */
  private String urlLlProperties;

  /** The url sync properties. */
  private String urlSyncProperties;

  /** The lab link. */
  private ILlConnection labLink;

  /** The service consumer. */
  private IDataPointConsumerService serviceConsumer;

  /** The lab link sync service. */
  private SyncClientServiceImpl labLinkSyncService;

  /** The local sync service. */
  private DatapointSyncConsumer labLinkLocalSyncConsumer;

  /** The initialized. */
  private boolean initialized = false;

  /** The plant uml source. */
  private String plantUmlSource = "\n@startuml\n";

  /** The Constant LABLINK_PROPERTIES. */
  private static final String LABLINK_PROPERTIES = "Lablink.properties";

  /** The Constant LABLINK_AIT. */
  private static final String LABLINK_AIT = "at.ac.ait";

  /** The Constant LOG. */
  private Logger log;

  private static final Logger LOG = LogManager.getLogger("DatapointBridge");

  /** The string data point consumers. */
  private Map<String, ConsumerString> stringDataPointConsumers;

  /** The double data point consumers. */
  private Map<String, ConsumerDouble> doubleDataPointConsumers;

  /** The long data point consumers. */
  private Map<String, ConsumerLong> longDataPointConsumers;

  /** The bool data point consumers. */
  private Map<String, ConsumerBoolean> boolDataPointConsumers;

  /** The complex data point consumers. */
  private Map<String, ConsumerComplex> complexDataPointConsumers;

  /** The clients uml. */
  private Map<String, String> clientsUml;

  /** The all data points. */
  private Map<String, DatapointDataTypes> allDataPoints;

  /** The config url. */
  private URL configUrl;

  /** The Constant IDENTITY. */
  private static final String IDENTITY = Utility.INFO_PRODUCT + " [" + Utility.VERSION + "]";

  /** The Constant GROUP_NAME_TAG. */
  private static final String GROUP_NAME_TAG = "GroupName";

  /** The Constant CLIENT_NAME_TAG. */
  private static final String CLIENT_NAME_TAG = "ClientName";

  /** The Constant SCENARIO_NAME_TAG. */
  private static final String SCENARIO_NAME_TAG = "ScenarioName";

  /** The Constant URL_LL_PROPERTIES. */
  private static final String URL_LL_PROPERTIES = "labLinkPropertiesUrl";

  /** The Constant URL_SYNC_PROPERTIES. */
  private static final String URL_SYNC_PROPERTIES = "syncHostPropertiesUrl";

  /** The Constant DATA_POINTS_TAG. */
  private static final String DATA_POINTS_TAG = "DataPoints";

  private static final String SIMULATOR_TAG = "Simulator";

  /** The Constant DP_ID_TAG. */
  private static final String DP_ID_TAG = "ID";

  /** The Constant DP_GROUP_NAME_TAG. */
  private static final String DP_GROUP_NAME_TAG = "DPGroupName";

  /** The Constant DP_CLIENT_NAME_TAG. */
  private static final String DP_CLIENT_NAME_TAG = "DPClientName";

  /** The Constant DP_NAME_TAG. */
  private static final String DP_NAME_TAG = "DPName";

  /** The Constant DP_IDENTIFIER_TAG. */
  private static final String DP_IDENTIFIER_TAG = "DPIdentifier";

  /** The Constant DP_DATATYPE_TAG. */
  private static final String DP_DATATYPE_TAG = "DPDataType";

  /** The Constant DP_CONNECTION_TAG. */
  private static final String DP_CONNECTION_TAG = "Connections";

  /** The Constant DP_CONNECTION_FROM_TAG. */
  private static final String DP_CONNECTION_FROM_TAG = "From";

  /** The Constant DP_CONNECTION_TO_TAG. */
  private static final String DP_CONNECTION_TO_TAG = "To";

  /** The Constant CONFIG_FILE_NAME. */
  private static final String CONFIG_FILE_NAME =
      "file:///" + System.getProperty("user.dir") + "dpbconfig.json";

  /** The Constant UML_CONFIG_PNG_NAME. */
  private static final String UML_CONFIG_PNG_NAME = System.getProperty("user.dir") + "\\";

  /** The Constant ALL_COMMENTS. */
  private static final String ALL_COMMENTS = "#.*#";

  /** The print status. */
  private Thread printStatus;
  private String configType = "JSON";

  /**
   * Instantiates a new datapoint bridge.
   *
   * @param url the url
   * @param type the type
   * @throws MalformedURLException the malformed URL exception
   */
  public DatapointBridge(String url, String type) throws MalformedURLException {
    this.configType = type;
    this.config(url);
  }

  /**
   * Instantiates a new DatapointBridge client.
   *
   * @throws FileNotFoundException the file not found exception
   * @throws ParseException the parse exception
   * @throws MalformedURLException the malformed URL exception
   */
  public DatapointBridge() throws FileNotFoundException, ParseException, MalformedURLException {

    LOG.info("Default constructor called.");
    this.config(CONFIG_FILE_NAME);

  }

  /**
   * Config.
   *
   * @param url the url
   * @throws MalformedURLException the malformed URL exception
   */
  private void config(String url) throws MalformedURLException {

    this.configUrl = new URL(url);

    // String
    stringDataPointConsumers = new HashMap<String, ConsumerString>();

    // Boolean
    boolDataPointConsumers = new HashMap<String, ConsumerBoolean>();

    // Long
    longDataPointConsumers = new HashMap<String, ConsumerLong>();

    // Double
    doubleDataPointConsumers = new HashMap<String, ConsumerDouble>();

    // Double
    complexDataPointConsumers = new HashMap<String, ConsumerComplex>();

    // Master
    allDataPoints = new HashMap<String, DatapointDataTypes>();

    printStatus = new Thread() {
      public void run() {
        checkConsumerStatus();
      }
    };

    printStatus.setName("ConsumerStatus");

  }

  private String ansiColorize(String text, String color) {
    return (color + text + ANSI_RESET);
  }

  /**
   * Check consumer status.
   */
  private void checkConsumerStatus() {

    while (true) {
      LOG.info("Checking consumer status...");

      this.doubleDataPointConsumers.forEach((key, val) -> {

        EDataPointConsumerState status = val.getConsumer().getState();
        String color =
            status == EDataPointConsumerState.CONNECTED 
                ? ansiColorize(status.toString(), ANSI_GREEN)
                : ansiColorize(status.toString(), ANSI_RED);

        LOG.debug("Consumer {} is {}.", key, color);

      });

      this.stringDataPointConsumers.forEach((key, val) -> {

        EDataPointConsumerState status = val.getConsumer().getState();
        String color =
            status == EDataPointConsumerState.CONNECTED 
                ? ansiColorize(status.toString(), ANSI_GREEN)
                : ansiColorize(status.toString(), ANSI_RED);

        LOG.debug("Consumer {} is {}.", key, color);

      });

      this.longDataPointConsumers.forEach((key, val) -> {

        EDataPointConsumerState status = val.getConsumer().getState();
        String color =
            status == EDataPointConsumerState.CONNECTED 
                ? ansiColorize(status.toString(), ANSI_GREEN)
                : ansiColorize(status.toString(), ANSI_RED);

        LOG.debug("Consumer {} is {}.", key, color);

      });

      this.boolDataPointConsumers.forEach((key, val) -> {

        EDataPointConsumerState status = val.getConsumer().getState();
        String color =
            status == EDataPointConsumerState.CONNECTED 
                ? ansiColorize(status.toString(), ANSI_GREEN)
                : ansiColorize(status.toString(), ANSI_RED);

        LOG.debug("Consumer {} is {}.", key, color);

      });

      this.complexDataPointConsumers.forEach((key, val) -> {

        EDataPointConsumerState status = val.getConsumer().getState();
        String color =
            status == EDataPointConsumerState.CONNECTED 
                ? ansiColorize(status.toString(), ANSI_GREEN)
                : ansiColorize(status.toString(), ANSI_RED);

        LOG.debug("Consumer {} is {}.", key, color);

      });

      try {
        Thread.sleep(5000);
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Setup Lablink client.
   *
   * @throws ConfigurationException the configuration exception
   */
  private void setupLablinkClient() throws ConfigurationException {
    // Lablink

    LOG.info("Setting up Lablink...");

    this.labLink = LlConnectionFactory.getDefaultConnectionController(LABLINK_AIT,
        this.scenarioName, this.groupName, this.clientName, this.getUrlLlProperties());

    LOG.info("Lablink client will be initialized with '" + LABLINK_AIT + "'," + "'"
        + this.scenarioName + "', '" + this.groupName + "', '" + this.clientName + "'.");

    LOG.info("Setting up DataPoint consumer services...");

    serviceConsumer = new DataPointConsumerServiceImpl(labLink, null);

    LOG.info("DataPoint Consumer Service created.");

    LOG.info("Setting up Sync services...");

    PropertiesConfiguration lablinkCfg = new PropertiesConfiguration(this.getUrlSyncProperties());

    this.labLinkSyncService = new SyncClientServiceImpl(this.labLink, lablinkCfg);

    this.labLinkLocalSyncConsumer = new DatapointSyncConsumer(IDENTITY);

    this.labLinkSyncService.registerSyncConsumer(labLinkLocalSyncConsumer);

    startLablink();

    LOG.info("Done.");

  }

  /**
   * Start Lablink.
   */
  private void startLablink() {
    LOG.info("Connecting to Lablink...");
    this.labLink.connect();

    LOG.info("Starting sync service...");
    this.labLinkSyncService.start();
  }

  /**
   * Register consumers.
   */
  private void registerConsumers() {
    LOG.info("Registering string consumers...");
    this.registerStringConsumers();

    LOG.info("Registering double consumers...");
    this.registerDoubleConsumers();

    LOG.info("Registering long consumers...");
    this.registerLongConsumers();

    LOG.info("Registering boolean consumers...");
    this.registerBooleanConsumers();

    LOG.info("Registering complex consumers...");
    this.registerComplexConsumers();
  }

  private String readConfigItem(JSONObject jsonObj, String key) {
    return (parseWithEnvironmentVariable(((String) jsonObj.get(key))));
  }

  /**
   * Read sourcce.
   *
   * @throws ParseException parsing exception
   * @throws ConfigurationException configuration exception
   * @throws IOException signals that an I/O exception has occurred
   */
  @SuppressWarnings("resource")
  private void readSourcce() throws ParseException, ConfigurationException, IOException {

    switch (this.configType.toUpperCase()) {
      case "JSON":
        LOG.info("Reading JSON configuration file..." + this.configUrl);

        String srcFileContents =
            new Scanner(this.configUrl.openStream()).useDelimiter("\\Z").next();
        srcFileContents = srcFileContents.replaceAll(ALL_COMMENTS, "");

        int still = srcFileContents.length() - srcFileContents.replace("#", "").length();
        if (still > 0) {
          throw new IllegalArgumentException("There are at least " + still
              + " line(s) with incorrectly started/terminated comments in the config file '"
              + CONFIG_FILE_NAME + "'.");
        }

        parseConfig(srcFileContents);
        break;
      case "PYTHON":
        LOG.info("Invoking Python interpreter to execute the script...");

        PythonInterpreter python = new PythonInterpreter();

        String pythonScript = new Scanner(this.configUrl.openStream()).useDelimiter("\\Z").next();

        LOG.debug("Provide Python cofiguration generation script is \n{}", pythonScript);

        PyObject config = new PyObject();

        try {
          python.execfile(this.configUrl.openStream());
          python.exec("config = DBPLablinkConfiguration()");
          python.exec("cfg = config.getJSONConfig()");
          config = python.get("cfg");
        } catch (Exception ex) {
          LOG.error("Error processing the Python script. Please, fix the errors and try again.");
          ex.printStackTrace();
          LOG.fatal("Lablink DPB service terminated.");
          System.exit(255);
        }

        parseConfig(config.asString());

        break;
      default:
        LOG.error("Invalid config type '{}'", this.configType.toUpperCase());
    }
  }

  private void parseConfig(String srcFileContents) throws ParseException, ConfigurationException {

    LOG.info("Parsing configuration file...");
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(srcFileContents);

    this.groupName = readConfigItem(jsonObject, GROUP_NAME_TAG);
    this.clientName = readConfigItem(jsonObject, CLIENT_NAME_TAG);
    this.scenarioName = readConfigItem(jsonObject, SCENARIO_NAME_TAG);
    this.urlLlProperties = readConfigItem(jsonObject, URL_LL_PROPERTIES);
    this.urlSyncProperties = readConfigItem(jsonObject, URL_SYNC_PROPERTIES);

    LOG.debug("Client Name: {}", this.clientName);
    LOG.debug("Group name: {}", this.groupName);
    LOG.debug("Scenario name: {}", this.scenarioName);
    LOG.debug("Lablink properties URI: {}", this.getUrlLlProperties());
    LOG.debug("Sync properties URI: {}", this.getUrlSyncProperties());

    this.plantUmlSource += "\ttitle '" + this.scenarioName + "' started on \\n" 
        + (new Date()).toString() + "\\n\\n\n";

    setupLablinkClient();

    {
      LOG.info("Processing datapoint consumers...");
      JSONArray watchItems = (JSONArray) jsonObject.get(DATA_POINTS_TAG);

      @SuppressWarnings("rawtypes")
      Iterator watchItemIter = watchItems.iterator();

      // create data point consumer
      while (watchItemIter.hasNext()) {
        JSONObject innerObj = (JSONObject) watchItemIter.next();

        String dpid = readConfigItem(innerObj, DP_ID_TAG);
        String dpgroup = readConfigItem(innerObj, DP_GROUP_NAME_TAG);
        String dpclient = readConfigItem(innerObj, DP_CLIENT_NAME_TAG);
        String dpname = readConfigItem(innerObj, DP_NAME_TAG);
        String dpidentifier = readConfigItem(innerObj, DP_IDENTIFIER_TAG);
        String dpdtype = readConfigItem(innerObj, DP_DATATYPE_TAG);

        DatapointDataTypes dt = DatapointDataTypes.fromId(dpdtype);
        boolean itemAdded = true;

        LOG.info("Processing '{}...", dpid);

        switch (dt) {
          case DATATYPE_STRING:
            this.stringDataPointConsumers.put(dpid,
                new ConsumerString(dpid, dpgroup, dpclient, dpname, dpidentifier));
            LOG.debug("STRING consumer added. id={}, group={}, client={}, name={}, identifier={}",
                dpid, dpgroup, dpclient, dpname, dpidentifier);
            break;
          case DATATYPE_LONG:
            this.longDataPointConsumers.put(dpid,
                new ConsumerLong(dpid, dpgroup, dpclient, dpname, dpidentifier));
            LOG.debug("LONG consumer added. id={}, group={}, client={}, name={}, identifier={}",
                dpid, dpgroup, dpclient, dpname, dpidentifier);
            break;
          case DATATYPE_DOUBLE:
            this.doubleDataPointConsumers.put(dpid,
                new ConsumerDouble(dpid, dpgroup, dpclient, dpname, dpidentifier));
            LOG.debug("DOUBLE consumer added. id={}, group={}, client={}, name={}, identifier={}",
                dpid, dpgroup, dpclient, dpname, dpidentifier);
            break;
          case DATATYPE_BOOL:
            this.boolDataPointConsumers.put(dpid,
                new ConsumerBoolean(dpid, dpgroup, dpclient, dpname, dpidentifier));
            LOG.debug("BOOL consumer added. id={}, group={}, client={}, name={}, identifier={}",
                dpid, dpgroup, dpclient, dpname, dpidentifier);
            break;
          case DATATYPE_COMPLEX:
            this.complexDataPointConsumers.put(dpid,
                new ConsumerComplex(dpid, dpgroup, dpclient, dpname, dpidentifier));
            LOG.debug("COMPLEX consumer added. id={}, group={}, client={}, name={}, identifier={}",
                dpid, dpgroup, dpclient, dpname, dpidentifier);
            break;
          default:
            itemAdded = false;
            LOG.warn("Datatype not '{}' supported yet.", dpdtype);
            break;
        }

        // add the item to master list
        if (itemAdded) {
          this.allDataPoints.put(dpid, dt);
          LOG.info(dpid + " added.");
          this.plantUmlSource += "\tcomponent [" + dpid + "]\n";
        } else {
          LOG.info(dpid + " ignored.");
        }

      } // First while loop ends
    }

    // create connections

    {
      LOG.info("Setting up datapoint bridges...");
      // Look for the individual DPs
      JSONArray connections = (JSONArray) jsonObject.get(DP_CONNECTION_TAG);

      @SuppressWarnings("rawtypes")
      Iterator connectionsIter = connections.iterator();

      // create data point consumer
      while (connectionsIter.hasNext()) {
        JSONObject connectionsInnerObj = (JSONObject) connectionsIter.next();

        String connfrom = (String) connectionsInnerObj.get(DP_CONNECTION_FROM_TAG);
        String connto = (String) connectionsInnerObj.get(DP_CONNECTION_TO_TAG);

        LOG.info("Processing " + connfrom + "-->" + connto + "...");

        this.plantUmlSource += "\t" + connfrom + "-->" + connto + "\n";

        if (!allDataPoints.containsKey(connfrom)) {
          throw new IllegalArgumentException(
              "A datapoint with ID='" + connfrom + "' does not exists.");
        }

        if (!allDataPoints.containsKey(connto)) {
          throw new IllegalArgumentException(
              "A datapoint (from) with ID='" + connto + "' does not exists.");
        }

        // get the data type of the source
        DatapointDataTypes conndt = allDataPoints.get(connfrom);

        // check if the data points have same data types
        if (!conndt.equals(allDataPoints.get(connto))) {
          throw new IllegalArgumentException("The source datapoint '" + connfrom
              + " and the destination datapoint '" + connto + " must be of the same datatype.");
        }

        // connect the two data points
        switch (conndt) {
          case DATATYPE_STRING:
            if (this.stringDataPointConsumers.get(connfrom).addNotifier(connto,
                this.stringDataPointConsumers.get(connto))) {
              LOG.info(
                  "A unidirectinal bridge is set from '" + connfrom + "' --> '" + connto + "'.");
            } else {
              LOG.info("A unidirectinal bridge between '" + connfrom + "' --> '" + connto
                  + "' already exists, IGNORED.");
            }
            break;
          case DATATYPE_LONG:
            if (this.longDataPointConsumers.get(connfrom).addNotifier(connto,
                this.longDataPointConsumers.get(connto))) {
              LOG.info(
                  "A unidirectinal bridge is set from '" + connfrom + "' --> '" + connto + "'.");
            } else {
              LOG.info("A unidirectinal bridge between '" + connfrom + "' --> '" + connto
                  + "' already exists, IGNORED.");
            }
            break;
          case DATATYPE_DOUBLE:
            if (this.doubleDataPointConsumers.get(connfrom).addNotifier(connto,
                this.doubleDataPointConsumers.get(connto))) {
              LOG.info(
                  "A unidirectinal bridge is set from '" + connfrom + "' --> '" + connto + "'.");
            } else {
              LOG.info("A unidirectinal bridge between '" + connfrom + "' --> '" + connto
                  + "' already exists, IGNORED.");
            }
            break;
          case DATATYPE_BOOL:
            if (this.boolDataPointConsumers.get(connfrom).addNotifier(connto,
                this.boolDataPointConsumers.get(connto))) {
              LOG.info(
                  "A unidirectinal bridge is set from '" + connfrom + "' --> '" + connto + "'.");
            } else {
              LOG.info("A unidirectinal bridge between '" + connfrom + "' --> '" + connto
                  + "' already exists, IGNORED.");
            }
            break;
          case DATATYPE_COMPLEX:
            if (this.complexDataPointConsumers.get(connfrom).addNotifier(connto,
                this.complexDataPointConsumers.get(connto))) {
              LOG.info(
                  "A unidirectinal bridge is set from '" + connfrom + "' --> '" + connto + "'.");
            } else {
              LOG.info("A unidirectinal bridge between '" + connfrom + "' --> '" + connto
                  + "' already exists, IGNORED.");
            }
            break;
          default:
            LOG.warn("Datatype '{}' not supported yet.", conndt);
            break;
        }
      } // second while loop
    }

    registerConsumers();

    this.plantUmlSource += "@enduml\n";
  }

  /**
   * Register string consumers.
   */
  private void registerStringConsumers() {
    for (Map.Entry<String, ConsumerString> con : this.stringDataPointConsumers.entrySet()) {
      con.getValue().register(this.serviceConsumer);
      LOG.info(con.getKey() + " registered.");
    }
  }

  /**
   * Register boolean consumers.
   */
  private void registerBooleanConsumers() {
    for (Map.Entry<String, ConsumerBoolean> con : this.boolDataPointConsumers.entrySet()) {
      con.getValue().register(this.serviceConsumer);
      LOG.info(con.getKey() + " registered.");
    }
  }

  /**
   * Register complex consumers.
   */
  private void registerComplexConsumers() {
    for (Map.Entry<String, ConsumerComplex> con : this.complexDataPointConsumers.entrySet()) {
      con.getValue().register(this.serviceConsumer);
      LOG.info(con.getKey() + " registered.");
    }
  }

  /**
   * Register double consumers.
   */
  private void registerDoubleConsumers() {
    for (Map.Entry<String, ConsumerDouble> con : this.doubleDataPointConsumers.entrySet()) {
      con.getValue().register(this.serviceConsumer);
      LOG.info(con.getKey() + " registered.");
    }
  }

  /**
   * Register long consumers.
   */
  private void registerLongConsumers() {
    for (Map.Entry<String, ConsumerLong> con : this.longDataPointConsumers.entrySet()) {
      con.getValue().register(this.serviceConsumer);
      LOG.info(con.getKey() + " registered.");
    }
  }

  /**
   * Initialize.
   *
   * @throws FileNotFoundException the file not found exception
   * @throws NullPointerException the null pointer exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   * @throws ConfigurationException the configuration exception
   */
  public void initialize() throws FileNotFoundException, NullPointerException, IOException,
      ParseException, ConfigurationException {
    readSourcce();
    // setupLablinkClient();
    this.setInitialized(true);
  }

  /**
   * Start.
   */
  public void start() {
    if (!this.isInitialized()) {
      LOG.error(this.getIdentity() + " has not been initialized yet.");
      return;
    }

    LOG.info("Waiting for other application(s)...");
    try {
      Thread.sleep(10000L);
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }

    this.printStatus.start();
  }

  /**
   * Stop.
   */
  public void stop() {
    // TODO Auto-generated method stub
  }

  /**
   * Pause.
   */
  public void pause() {
    // TODO Auto-generated method stub
  }

  /**
   * Resume.
   */
  public void resume() {
    // TODO Auto-generated method stub
  }

  /**
   * Gets the identity.
   *
   * @return the identity
   */
  public String getIdentity() {
    return IDENTITY;
  }

  /**
   * Prints the configuration.
   *
   * @return the string
   */
  public String printConfiguration() {

    String filename = UML_CONFIG_PNG_NAME
        + (((new Timestamp(System.currentTimeMillis()).toString().replaceAll(":", "-"))
            .replaceAll("-", "")).replaceAll(" ", "")).replaceAll("[.]", "-")
        + ".png";

    File pngfile = new File(filename);
    try {
      FileOutputStream png = new FileOutputStream(pngfile);
      SourceStringReader reader = new SourceStringReader(this.plantUmlSource);
      try {
        String desc = reader.generateImage(png);
        LOG.info("The description is " + desc);
      } catch (IOException ex) {
        ex.printStackTrace();
      } finally {
        try {
          png.close();
        } catch (IOException ex) {
          // TODO Auto-generated catch block
          ex.printStackTrace();
        }
      }
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    return "Configuration drawn to " + UML_CONFIG_PNG_NAME + filename;
  }

  /**
   * Checks if this instance is initialized.
   *
   * @return true, if is initialized
   */
  public boolean isInitialized() {
    return initialized;
  }

  /**
   * Sets the initialization flag.
   *
   * @param initialized the new initialization status
   */
  private void setInitialized(boolean initialized) {
    this.initialized = initialized;
  }

  /**
   * Getter for the URL to the Lablink properties.
   *
   * @return the urlLlProperties
   */
  public String getUrlLlProperties() {
    return urlLlProperties;
  }

  /**
   * Setter for the URL to the Lablink properties.
   *
   * @param urlLlProperties the urlLlProperties to set
   */
  private void setUrlLlProperties(String urlLlProperties) {
    this.urlLlProperties = parseWithEnvironmentVariable(urlLlProperties);
  }

  /**
   * Getter for the URL to the sync properties.
   *
   * @return the urlSyncProperties
   */
  public String getUrlSyncProperties() {
    return urlSyncProperties;
  }

  /**
   * Setter for the URL to the sync properties.
   *
   * @param urlSyncProperties the urlSyncProperties to set
   */
  private void setUrlSyncProperties(String urlSyncProperties) {
    this.urlSyncProperties = parseWithEnvironmentVariable(urlSyncProperties);
  }

  /**
   * Parses a config string containing an environment variable.
   *
   * @param input the input
   * @return the string
   */
  private String parseWithEnvironmentVariable(String input) {
    String output = null;
    String ev = StringUtils.substringBetween(input, "%", "%");
    if (StringUtils.isEmpty(ev)) {
      output = input;
    } else {
      String evVal = System.getenv(ev);
      if (evVal == null) {
        output = input;
      } else {
        output = input.replace("%" + ev + "%", evVal);
      }
    }

    return output;
  }
}

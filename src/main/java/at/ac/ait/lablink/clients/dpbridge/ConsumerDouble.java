//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
// Distributed under the terms of the Modified BSD License.
//

package at.ac.ait.lablink.clients.dpbridge;

import at.ac.ait.lablink.core.service.datapoint.consumer.EDataPointConsumerState;
import at.ac.ait.lablink.core.service.datapoint.consumer.IDataPointConsumer;
import at.ac.ait.lablink.core.service.datapoint.consumer.IDataPointConsumerNotifier;
import at.ac.ait.lablink.core.service.datapoint.consumer.IDataPointConsumerService;
import at.ac.ait.lablink.core.service.datapoint.consumer.impl.DoubleDataPointConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class ConsumerDouble.
 */
public class ConsumerDouble {

  /** The logger. */
  private static final Logger LOG = LogManager.getLogger("ConsumerDouble");

  /** The consumer. */
  private DoubleDataPointConsumer consumer;

  /** The notifiers. */
  private Map<String, ConsumerDouble> notifiers;

  /** The name. */
  private String name;

  /**
   * Notify update.
   *
   * @param newvalue the newvalue
   */
  private void notifyUpdate(double newvalue) {
    LOG.debug("Initiating to notify other " + notifiers.size() + " for change in " + this.name);
    for (Map.Entry<String, ConsumerDouble> con : notifiers.entrySet()) {
      con.getValue().updateValue(newvalue);
      LOG.info(con.getKey() + " is notified about the changes in " + this.name + ".");
    }
  }

  /**
   * Creates the consumer.
   *
   * @param group the group
   * @param client the client
   * @param dpname the dpname
   * @param identifier the identifier
   */
  private void createConsumer(String group, String client, String dpname, String identifier) {
    consumer = new DoubleDataPointConsumer(group, client, Arrays.asList(dpname, identifier));

    consumer.setNotifier(new IDataPointConsumerNotifier<Double>() {
      @Override
      public void valueUpdate(IDataPointConsumer<Double> dataPointConsumer) {
        LOG.debug("Consumer '" + getName() + "' gets a new value: " + dataPointConsumer.getValue());
        // Propagate the change

        notifyUpdate(dataPointConsumer.getValue());
      }

      @Override
      public void stateChanged(IDataPointConsumer<Double> dataPointConsumer) {
        LOG.info("Consumer '" + getName() + "' changed state: " + dataPointConsumer.getState()
            + "  " + (dataPointConsumer.getState() == EDataPointConsumerState.CONNECTED
                ? dataPointConsumer.getValue() : ""));
      }

    });
  }

  /**
   * Instantiates a new consumer string.
   *
   * @param name the name
   * @param group the group
   * @param client the client
   * @param dpname the data point name
   * @param identifier the identifier
   */
  public ConsumerDouble(String name, String group, String client, String dpname,
      String identifier) {
    this.name = name;
    createConsumer(group, client, dpname, identifier);
    notifiers = new HashMap<String, ConsumerDouble>();
  }

  /**
   * Gets the consumer.
   *
   * @return the consumer
   */
  public DoubleDataPointConsumer getConsumer() {
    return consumer;
  }

  /**
   * Adds the notifier.
   *
   * @param id the id
   * @param notifier the notifier
   * @return true, if successful
   */
  public boolean addNotifier(String id, ConsumerDouble notifier) {

    // check for duplicate
    boolean success = !(notifiers.containsKey(id));

    if (success) {
      notifiers.put(id, notifier);
      LOG.debug("Another notifier " + id + " added. The total notifiers now are " + notifiers.size()
          + " for " + this.name);
    }
    return success;
  }

  /**
   * Register.
   *
   * @param service the service
   */
  public void register(IDataPointConsumerService service) {
    service.registerDatapointConsumer(consumer);
  }

  /**
   * Update value.
   *
   * @param newVal the new value
   */
  public void updateValue(double newVal) {
    this.consumer.setValue(newVal);
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }
}

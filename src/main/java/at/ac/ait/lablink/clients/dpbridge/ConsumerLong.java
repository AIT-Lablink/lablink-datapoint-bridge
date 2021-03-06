//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
// Distributed under the terms of the Modified BSD License.
//

package at.ac.ait.lablink.clients.dpbridge;

import at.ac.ait.lablink.core.service.datapoint.consumer.EDataPointConsumerState;
import at.ac.ait.lablink.core.service.datapoint.consumer.IDataPointConsumer;
import at.ac.ait.lablink.core.service.datapoint.consumer.IDataPointConsumerNotifier;
import at.ac.ait.lablink.core.service.datapoint.consumer.IDataPointConsumerService;
import at.ac.ait.lablink.core.service.datapoint.consumer.impl.LongDataPointConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class ConsumerLong.
 */
public class ConsumerLong {

  /** The logger. */
  private static final Logger LOG = LogManager.getLogger("ConsumerLong");

  /** The consumer. */
  private LongDataPointConsumer consumer;

  /** The notifiers. */
  private Map<String, ConsumerLong> notifiers;

  /** The name. */
  private String name;

  /**
   * Notify update.
   *
   * @param newvalue the newvalue
   */
  private void notifyUpdate(long newvalue) {
    LOG.debug("Initiating to notify other " + notifiers.size() + " for change in " + this.name);
    for (Map.Entry<String, ConsumerLong> con : notifiers.entrySet()) {
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
    consumer = new LongDataPointConsumer(group, client, Arrays.asList(dpname, identifier));

    consumer.setNotifier(new IDataPointConsumerNotifier<Long>() {
      @Override
      public void valueUpdate(IDataPointConsumer<Long> dataPointConsumer) {
        LOG.debug("Consumer gets a new value:" + dataPointConsumer.getValue());
        // Propagate the change
        notifyUpdate(dataPointConsumer.getValue());
      }

      @Override
      public void stateChanged(IDataPointConsumer<Long> dataPointConsumer) {
        LOG.debug("Consumer changed state: " + dataPointConsumer.getState() + "  "
            + (dataPointConsumer.getState() == EDataPointConsumerState.CONNECTED
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
  public ConsumerLong(String name, String group, String client, String dpname, String identifier) {
    this.name = name;
    createConsumer(group, client, dpname, identifier);
    notifiers = new HashMap<String, ConsumerLong>();
  }

  /**
   * Gets the consumer.
   *
   * @return the consumer
   */
  public LongDataPointConsumer getConsumer() {
    return consumer;
  }

  /**
   * Adds the notifier.
   *
   * @param id the id
   * @param notifier the notifier
   * @return true, if successful
   */
  public boolean addNotifier(String id, ConsumerLong notifier) {

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
  public void updateValue(long newVal) {
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

//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
// Distributed under the terms of the Modified BSD License.
//

package at.ac.ait.lablink.clients.dpbridge;

import at.ac.ait.lablink.core.service.sync.ISyncParameter;
import at.ac.ait.lablink.core.service.sync.consumer.ISyncConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class DatapointSyncConsumer.
 */
public class DatapointSyncConsumer implements ISyncConsumer {

  /** The logger. */
  private static Logger logger = LogManager.getLogger("ISyncConsumer");

  /** The client name. */
  private String clientName;

  /**
   * Instantiates a new dp sync consumer.
   *
   * @param cname the client name
   */
  public DatapointSyncConsumer(String cname) {
    this.setClientName(cname);
  }

  /**
   * @see at.ac.ait.lablink.core.service.sync.consumer.ISyncConsumer#init(
   * at.ac.ait.lablink.core.service.sync.ISyncParameter
   * )
   */
  @Override
  public boolean init(ISyncParameter scs) {
    logger.debug(">>> Intialize Sync Client " + this.getClientName());
    logger.debug(">>> SCS PARAMS: " + scs.getSimMode() + " " + scs.getScaleFactor() + " "
        + scs.getSimBeginTime() + " " + scs.getSimEndTime() + " " + scs.getStepSize());
    return true;
  }

  /**
   * @see at.ac.ait.lablink.core.service.sync.consumer.ISyncConsumer#go(
   * long, long, at.ac.ait.lablink.core.service.sync.ISyncParameter
   * )
   */
  @Override
  public long go(long curtime, long until, ISyncParameter scs) {

    long goStart = System.currentTimeMillis();

    logger.debug(">>> Reqired processing time for current step: "
        + (System.currentTimeMillis() - goStart) + " ms.");

    return (until + scs.getStepSize());
  }

  /**
   * @see at.ac.ait.lablink.core.service.sync.consumer.ISyncConsumer#stop(
   * at.ac.ait.lablink.core.service.sync.ISyncParameter
   * )
   */
  @Override
  public boolean stop(ISyncParameter scs) {
    logger.debug(">>> Sync Client " + getClientName() + " stopped!");
    return true;
  }

  /**
   * Getter for the client name.
   *
   * @return the client name
   */
  private String getClientName() {
    return clientName;
  }

  /**
   * Setter for the client name.
   *
   * @param cname the new c name
   */
  private void setClientName(String cname) {
    this.clientName = cname;
  }

}

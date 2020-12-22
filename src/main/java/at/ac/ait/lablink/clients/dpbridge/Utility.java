//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
// Distributed under the terms of the Modified BSD License.
//

package at.ac.ait.lablink.clients.dpbridge;

import at.ac.ait.lablink.clients.dpbridge.Version;

/**
 * The Class Utility.
 */
public class Utility {

  /** The info product ASCII art. */
  public static final String INFO_PRODUCT_ASCII_ART = "\n"
      + "░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░\n"
      + "░░░█████╗░██╗████████╗░░░██╗░░░░░░█████╗░██████╗░██╗░░░░░██╗███╗░░██╗██╗░░██╗░░\n"
      + "░░██╔══██╗██║╚══██╔══╝░░░██║░░░░░██╔══██╗██╔══██╗██║░░░░░██║████╗░██║██║░██╔╝░░\n"
      + "░░███████║██║░░░██║░░░░░░██║░░░░░███████║██████╔╝██║░░░░░██║██╔██╗██║█████╔╝░░░\n"
      + "░░██╔══██║██║░░░██║░░░░░░██║░░░░░██╔══██║██╔══██╗██║░░░░░██║██║╚████║██╔═██╗░░░\n"
      + "░░██║░░██║██║░░░██║░░░░░░███████╗██║░░██║██████╔╝███████╗██║██║░╚███║██║░╚██╗░░\n"
      + "░░╚═╝░░╚═╝╚═╝░░░╚═╝░░░░░░╚══════╝╚═╝░░╚═╝╚═════╝░╚══════╝╚═╝╚═╝░░╚══╝╚═╝░░╚═╝░░\n"
      + "░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░\n\n";

  public static final String INFO_PRODUCT = "AIT Lablink Datapoint Bridge";

  public static final String VERSION = Version.getVersion();

  public static final String INFO_COPYRIGHTS = "Copyright © 2020";

  public static final String INFO_WEBSITE = "https://ait-lablink.readthedocs.io/";

  public static final String INFO_ORGANIZATION = "AIT Austrian Institute of Technology GmbH";

  public static final String INFO_LICENSE = "Distributed under the terms of the "
      + "Modified BSD License";

  public static final String INFO_COPYRIGHTS_TEXT = INFO_PRODUCT_ASCII_ART
      + INFO_PRODUCT + " [" + VERSION + "]\n" + INFO_COPYRIGHTS + " " + INFO_ORGANIZATION
      + ".\n" + INFO_LICENSE + ".\n" + "Visit " + INFO_WEBSITE + " for more information.\n";
}

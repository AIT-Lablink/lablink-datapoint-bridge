//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
// Distributed under the terms of the Modified BSD License.
//

package at.ac.ait.lablink.clients.dpbridge;

/**
 * Enum SocketDataTypes.
 */
public enum DatapointDataTypes {

  /** The datatype long. */
  DATATYPE_LONG("long"),

  /** The datatype bool. */
  DATATYPE_BOOL("bool"),

  /** The datatype string. */
  DATATYPE_STRING("string"),

  /** The datatype double. */
  DATATYPE_DOUBLE("double");

  /** The value. */
  private String value;

  /**
   * Instantiates a new socket data types.
   *
   * @param val the val
   */
  private DatapointDataTypes(String val) {
    this.value = val;
  }

  /**
   * Getter for the id.
   *
   * @return the id
   */
  public String getId() {
    return this.value;
  }

  /**
   * Get datapoint type from datapoint ID.
   *
   * @param id the datapoint ID
   * @return the datapoint type
   */
  public static DatapointDataTypes fromId(String id) {
    for (DatapointDataTypes type : DatapointDataTypes.values()) {
      if (type.getId().equals(id)) {
        return type;
      }
    }
    System.out.println("Invalid Id '" + id + "'.");
    return null;
  }

}


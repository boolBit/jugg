package com.lorabit.rpc.base;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class ClientId {
  private final String name;
  private final boolean idClient;

  public ClientId(String name, boolean isClient) {
    if(name == null) {
      throw new IllegalArgumentException("name==null");
    }
    this.name = name;
    this.idClient = isClient;
  }

  public String getName() {
    return name;
  }

  public boolean isIdClient() {
    return idClient;
  }
}

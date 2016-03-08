package com.lorabit.rpc.base;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

/**
 * @author lorabit
 * @since 16-3-7
 */
@Data
public class RpcConfig implements Serializable {

  private static final long serialVersionUID = -7109603572053531770L;

  private Map<String, Serializable> data = Maps.newHashMap();

  public boolean isValid() {
    return data != null;
  }

  @Override
  public String toString() {
    return "";
  }

  public Serializable getConf(String key) {
    return data.get(key);
  }

  public void addConf(String key, Serializable data) {
    this.data.put(key, data);
  }

}

package com.lorabit.rpc.router;

import java.util.Map;

/**
 * @author lorabit
 * @since 16-3-7
 */
public interface IOBalance {

  public String next(String token);

  public void updateLoad(Map<String, Integer> load);

  public void fail(String token);
}


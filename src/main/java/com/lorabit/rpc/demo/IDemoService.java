package com.lorabit.rpc.demo;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-3-9
 */
public interface IDemoService {
  String now();

  void exception() throws Exception;

  Map<Object, Object> getMap();

  List<Object> getList();
}

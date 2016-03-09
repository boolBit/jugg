package com.lorabit.util;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-3-9
 */
public class TestObject implements Serializable {


  String name = "lining";
  int age = 22;
  List<String> likes = Lists.newArrayList("sport", "dota2", "fodd");
  Map<Object, Object> maps = new HashMap<>();

  {
    maps.put("test", 123);
    maps.put(4334, Lists.newArrayList(1, 2, 3));
  }


  Object obj = null;


}

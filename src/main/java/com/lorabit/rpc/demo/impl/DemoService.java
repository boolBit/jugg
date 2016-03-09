package com.lorabit.rpc.demo.impl;

import com.google.common.collect.Lists;

import com.lorabit.rpc.demo.IDemoService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-3-9
 */
public class DemoService implements IDemoService {

  SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Override
  public String now() {
    return "now is " + dft.format(new Date());
  }

  @Override
  public void exception() throws Exception {
    throw new IllegalArgumentException("your input is not illegal");
  }

  @Override
  public Map<Object, Object> getMap() {
    Map map = new HashMap();
    map.put("age", 22);
    map.put("like", Lists.newArrayList(1, 2, 3));
    return map;
  }

  @Override
  public List<Object> getList() {
    List<Object> ret = Lists.newArrayList();
    ret.add(new Exception("aaa"));
    ret.add("xxxxx");
    Map m = new HashMap();
    m.put("1", 234);
    ret.add(m);
    return ret;
  }
}

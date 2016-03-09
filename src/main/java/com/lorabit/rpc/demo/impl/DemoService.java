package com.lorabit.rpc.demo.impl;

import com.lorabit.rpc.demo.IDemoService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lorabit
 * @since 16-3-9
 */
public class DemoService implements IDemoService {

  SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Override
  public String now() {
    return "now is " + dft.format(new Date()) ;
  }
}

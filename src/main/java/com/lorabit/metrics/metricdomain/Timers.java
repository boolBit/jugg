package com.lorabit.metrics.metricdomain;

import lombok.Data;

/**
 * @author lorabit
 * @since 16-2-24
 */
@Data
public class Timers {
  long count;
  double max;
  double mean;
  double min;
  double p50;
  double p75;
  double p95;
  double p98;
  double p99;
  double p999;
  double stddev;
  double m15_rate;
  double m1_rate;
  double m5_rate;
}

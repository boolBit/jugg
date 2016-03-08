package com.lorabit.metrics.metricdomain;

import lombok.Data;

/**
 * @author lorabit
 * @since 16-2-24
 */
@Data
public class Histograms {
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
}

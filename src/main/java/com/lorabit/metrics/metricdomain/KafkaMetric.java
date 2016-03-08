package com.lorabit.metrics.metricdomain;

import java.util.Map;

import lombok.Data;

/**
 * @author lorabit
 * @since 16-2-24
 */
@Data
public class KafkaMetric {
  String version;
  Map<String, Gauges> gauges;
  Map<String, KCounters> counters;
  Map<String, Histograms> histograms;
  Map<String, Meters> meters;
  Map<String, Timers> timers;
}

package com.lorabit.metrics.metricdomain;

import lombok.Data;

/**
 * @author lorabit
 * @since 16-2-24
 */
@Data
public class Meters {
  long count;
  double m15_rate;
  double m1_rate;
  double m5_rate;
  double mean_rate;
}

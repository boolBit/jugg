package com.lorabit.metrics.report;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-2-25
 */
public interface Reporter {
  void report(List<Map> infos);
}

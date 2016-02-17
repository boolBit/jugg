package com.lorabit.cloud;

import java.util.Map;

/**
 * @author lorabit
 * @since 16-2-17
 */
public interface Task {
  String getTaskName();
  TaskContext getTaskContext();

  public void trigger();

  String getCronExpr();

  void exe(TriggerType cron);
  void exe(TriggerType cron,String msg);
  void exe(TriggerType cron,Map<String,Object> externalData);

  enum TriggerType{
    CONTAINER,
    CRON,
    MSG,
    MANAUAL,
  }
}

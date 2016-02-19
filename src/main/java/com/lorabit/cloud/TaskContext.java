package com.lorabit.cloud;

import java.util.HashMap;

/**
 * @author lorabit
 * @since 16-2-17
 */
public class TaskContext extends HashMap {
  public static final String DESC = "_desc";
  public static final String TASK_NAME = "task_name";
  public static final String MSG = "_msg";
  public static final String TYPE = "_trigger_type";
  public static final String TOPICS = "_topics";
  public static final String CRON = "_cron";
  public static final String TIMEOUT_NUM = "_time_out_num";


  public Object getTaskName() {
    return this.get(TASK_NAME);
  }

  public Object getDesc() {
    return this.get(DESC);
  }

  public Task.TriggerType getTriggerType() {
    return (Task.TriggerType) this.get(TYPE);
  }

  public Object getTopics() {
    return this.get(TOPICS);
  }

  public Object getMsg() {
    return this.get(MSG);
  }

  public Object getCron() {
    return this.get(CRON);
  }

  public int getTimeOut() {
    if (this.get(TIMEOUT_NUM) == null) return 0;
    return (Integer) this.get(TIMEOUT_NUM);
  }

}

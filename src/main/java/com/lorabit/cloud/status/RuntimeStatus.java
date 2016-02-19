package com.lorabit.cloud.status;

import com.google.common.collect.ImmutableMap;

import com.lorabit.cloud.Task;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lorabit
 * @since 16-2-18
 */
public class RuntimeStatus {

  private ReentrantLock lock = new ReentrantLock();

  private static volatile AtomicReference<RuntimeStatus> status = new
      AtomicReference<>();

  static class TriggerStat implements Serializable{
    private AtomicInteger triggerCount = new AtomicInteger(0);
    private AtomicInteger errorCount = new AtomicInteger(0);
    private AtomicInteger runningCount = new AtomicInteger(0);
    private Date lastTriggerDate = null;
    private String status;

    public AtomicInteger getTriggerCount() {
      return triggerCount;
    }

    public AtomicInteger getErrorCount() {
      return errorCount;
    }

    public AtomicInteger getRunningCount() {
      return runningCount;
    }

    public Date getLastTriggerDate() {
      return lastTriggerDate;
    }

    public String getStatus() {
      return status;
    }
  }

  static class TaskInfo implements Serializable{
    private String taskName;
    private List<String> topics;
    private String cron;
    private String type;

    public String getTaskName() {
      return taskName;
    }

    public List<String> getTopics() {
      return topics;
    }

    public String getCron() {
      return cron;
    }

    public String getType() {
      return type;
    }
  }

  private Map<String, TriggerStat> triggerStats = new HashMap<>();
  private Map<String, TaskInfo> taskInfos = new HashMap<>();

  public static RuntimeStatus get() {
    RuntimeStatus ret = status.get();
    if (ret == null) {
      status.compareAndSet(null, new RuntimeStatus());
      ret = status.get();
    }
    return ret;
  }

  public void enter(String taskname) {
    TriggerStat triggerStat = getsert(taskname);
    triggerStat.runningCount.incrementAndGet();
  }

  public void leave(String taskname) {
    TriggerStat triggerStat = getsert(taskname);
    triggerStat.runningCount.decrementAndGet();
  }

  public void error(String taskname) {
    TriggerStat triggerStat = getsert(taskname);
    triggerStat.errorCount.incrementAndGet();
  }

  private TriggerStat getsert(String taskName) {
    if (!triggerStats.containsKey(taskName)) {
      lock.lock();
      if (!triggerStats.containsKey(taskName))
        triggerStats.put(taskName, new TriggerStat());
      lock.unlock();
    }
    return triggerStats.get(taskName);
  }

  public void register(Task task) {
    TaskInfo info = new TaskInfo();
    info.taskName = task.getTaskName();
    info.cron = task.getCronExpr();
    info.topics = task.getTopics();
    info.type = task.getTriggerType().name();
    taskInfos.put(task.getTaskName(), info);
  }

  public void unRegister(Task task) {
    taskInfos.remove(task.getTaskName());
  }

  public void record(ExecDetailDO detail) {
    TriggerStat triggerStat = getsert(detail.getTaskName());
    triggerStat.lastTriggerDate = new Date();
    triggerStat.triggerCount.incrementAndGet();
  }

  public Map dumpTriggerStat(){
    return ImmutableMap.copyOf(triggerStats);
  }

  public Map dumpTaskInfos(){
    return ImmutableMap.copyOf(taskInfos);
  }

}

package com.lorabit.cloud;

import com.lorabit.cloud.job.AbstractJob;
import com.lorabit.cloud.scheduler.TaskHelper;
import com.lorabit.cloud.status.RuntimeStatus;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

/**
 * @author lorabit
 * @since 16-2-17
 */
public class BaseTask implements Task {

  @Resource
  TaskHelper taskHelper;
  @Resource
  RuntimeConfig runtimeConfig;

  public static Map<String, Task> tasks = new HashMap();

  private String taskName;
  private String description;
  private String cronExpr;
  private TriggerType type;
  private int timeoutNum;

  private AbstractJob job;
  private Map<String, Object> extData;

  private List<String> topics;
  private int parallelNum;
  private String groupId;
  private boolean halt;

  @Override
  public String getTaskName() {
    return taskName;
  }


  @Override
  public TaskContext getTaskContext() {
    TaskContext context = new TaskContext();
    context.put(TaskContext.DESC, description);
    context.put(TaskContext.TASK_NAME, getTaskName());
    context.put(TaskContext.TYPE, type);
    context.put(TaskContext.TIMEOUT_NUM,timeoutNum);
    if (extData != null && extData.size() > 0) {
      context.putAll(extData);
    }
    return context;
  }

  @Override
  public void trigger() {
    switch (type) {
      case CRON:
        cron();
        break;

      case MANAUAL:
      case CONTAINER:
        Thread t = new Thread(new Runnable() {
          @Override
          public void run() {
            exe(type);
          }
        }, "container_job_" + getTaskName());
        t.start();
        break;

      case MSG:
        final ConsumerConnector connector = Consumer.createJavaConsumerConnector(createConsumerConfig());
        HashMap<String, Integer> m = new HashMap();
        for (String topic : topics) {
          m.put(topic, new Integer(parallelNum));
        }
        final Map<String, List<KafkaStream<byte[], byte[]>>> streams = connector.createMessageStreams(m);
        for (String topic : topics) {
          for (int index = 0; index < parallelNum; index++) {
            KafkaStream<byte[], byte[]> stream = streams.get(topic).get(index);
            final ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
            new Thread("_kfk_thread_" + taskName + "_" + topic + "_" + index) {
              @Override
              public void run() {
                while (!runtimeConfig.isHalt() && !halt && iterator.hasNext()) {
                  MessageAndMetadata<byte[], byte[]> msg1 = iterator.next();
                  String msg = new String(msg1.message());
                  exe(type, msg);
                }
                connector.shutdown();
              }
            }.start();
          }
        }
        break;
      default:
        return;
    }
    /** hold task **/
    tasks.remove(taskName);
    tasks.put(taskName, this);
    /** register task info**/
    RuntimeStatus.get().register(this);
  }

  private void cron() {
    taskHelper.addTask(this);
  }

  @Override
  public String getCronExpr() {
    return cronExpr;
  }

  @Override
  public List<String> getTopics() {
    return topics;
  }

  @Override
  public TriggerType getTriggerType() {
    return type;
  }

  @Override
  public void exe(TriggerType cron) {
    job.exe(getTaskContext());
  }

  @Override
  public void exe(TriggerType cron, String msg) {
    TaskContext context = getTaskContext();
    context.put(TaskContext.MSG, msg);
    job.exe(context);
  }

  @Override
  public void exe(TriggerType cron, Map<String, Object> externalData) {
    TaskContext context = getTaskContext();
    context.putAll(externalData);
    job.exe(context);
  }

  private ConsumerConfig createConsumerConfig() {
    String mxName = ManagementFactory.getRuntimeMXBean().getName();
    Properties props = new Properties();
    props.put("consumer.id", this.taskName + '-' + mxName.replace('@', '-'));
    props.put("zookeeper.connect", "localhost:2181");
    props.put("group.id", groupId);
    props.put("zookeeper.session.timeout.ms", "8000");
    props.put("zookeeper.sync.time.ms", "500");
    props.put("auto.commit.interval.ms", "1000");
    props.put("refresh.leader.backoff.ms", "10000");

    return new ConsumerConfig(props);
  }

  public void setTaskHelper(TaskHelper taskHelper) {
    this.taskHelper = taskHelper;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCronExpr(String cronExpr) {
    this.cronExpr = cronExpr;
  }

  public void setType(TriggerType type) {
    this.type = type;
  }

  public void setJob(AbstractJob job) {
    this.job = job;
  }

  public void setTopics(List<String> topics) {
    this.topics = topics;
  }

  public void setExtData(Map<String, Object> extData) {
    this.extData = extData;
  }

  public void setRuntimeConfig(RuntimeConfig runtimeConfig) {
    this.runtimeConfig = runtimeConfig;
  }

  public void setParallelNum(int parallelNum) {
    this.parallelNum = parallelNum;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public void setHalt(boolean halt) {
    this.halt = halt;
  }
}

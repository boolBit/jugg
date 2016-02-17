package com.lorabit.cloud.job;

import com.lorabit.cloud.Task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author lorabit
 * @since 16-2-17
 */
public class ProxyJob implements Job {

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    Task task = (Task) context.getTrigger().getJobDataMap().get("_task");
    task.exe(Task.TriggerType.CRON);
  }
}

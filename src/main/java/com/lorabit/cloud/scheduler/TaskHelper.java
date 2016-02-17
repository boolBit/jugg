package com.lorabit.cloud.scheduler;

import com.lorabit.cloud.Task;
import com.lorabit.cloud.job.ProxyJob;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.TriggerBuilder;

import javax.annotation.Resource;

/**
 * @author lorabit
 * @since 16-2-17
 */
public class TaskHelper {
  @Resource
  TaskScheduler taskScheduler;

  public void addTask(Task task) {
    JobDataMap data = new JobDataMap();
    data.put("_task", task);

    org.quartz.Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity(task.getTaskName())
        .startNow()
        .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpr()))
        .build();
    JobDetail job = JobBuilder.newJob(ProxyJob.class)
        .withIdentity(new JobKey(task.getTaskName()))
        .usingJobData(data)
        .build();

    taskScheduler.remove(task.getTaskName());
    taskScheduler.schedule(job, trigger);

  }


}

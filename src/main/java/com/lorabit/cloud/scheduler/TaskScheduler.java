package com.lorabit.cloud.scheduler;

import com.lorabit.cloud.RuntimeConfig;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * @author com.lorabit
 * @since 16-2-16
 */
public class TaskScheduler implements InitializingBean, Observer {
  Scheduler scheduler;

  @Override
  public void afterPropertiesSet() throws Exception {
    SchedulerFactory factory = new StdSchedulerFactory("/home/hellokitty/桌面/quartz.prop");
    scheduler = factory.getScheduler();
    scheduler.startDelayed(1);
  }

  public void schedule(JobDetail job, Trigger trigger) {
    try {
      scheduler.scheduleJob(job, trigger);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  public void remove(String jobname) {
    try {
      scheduler.deleteJob(new JobKey(jobname));
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  public List<Map<String, Object>> getCurrentRunInfo() {
    List<Map<String, Object>> infos = new ArrayList<>();
    try {
      List<JobExecutionContext> jobContexts = scheduler.getCurrentlyExecutingJobs();
      for (JobExecutionContext jobContext : jobContexts) {
        Map<String, Object> info = new HashMap();
        info.put("job name", jobContext.getJobDetail().getKey());
        info.put("run time", jobContext.getJobRunTime());
        infos.add(info);
      }
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
    return infos;
  }


  public void halt() {
    try {
      scheduler.shutdown(false);
      System.out.println("scheduler shutdown already");
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof RuntimeConfig) {
      if (((RuntimeConfig) o).isHalt()) {
        halt();
      }
    }
  }
}

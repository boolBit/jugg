package spring.cloud;

import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author com.lorabit
 * @since 16-2-16
 */
public class qurz {

  @Test
  public void t() throws SchedulerException {
    Scheduler scheduler;
    System.out.println("main id" + Thread.currentThread().getId());
    SchedulerFactory factory = new StdSchedulerFactory("/home/hellokitty/桌面/quartz.prop");
    scheduler = factory.getScheduler();
    scheduler.startDelayed(1);

    org.quartz.Trigger trigger = TriggerBuilder.newTrigger().startNow().build();
    org.quartz.Trigger trigger2 = TriggerBuilder.newTrigger().startNow().build();
    JobDetail job = JobBuilder.newJob(DemoJob.class).withIdentity(new JobKey("test")).build();
    JobDetail job2 = JobBuilder.newJob(DemoJob.class).withIdentity(new JobKey("test2")).build();

    scheduler.scheduleJob(job, trigger);
    scheduler.scheduleJob(job2, trigger2);

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("kill");
    System.out.println(scheduler.getCurrentlyExecutingJobs().size());
//    scheduler.deleteJobs(new JobKey("test");
//    scheduler.interrupt(new JobKey("test"));
//    scheduler.interrupt(new JobKey("test2"));
//    System.out.println(scheduler.getCurrentlyExecutingJobs().size());
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("over");
  }
}

package spring.cloud;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author com.lorabit
 * @since 16-2-16
 */
public class DemoJob implements InterruptableJob {

  Callable callable;

  @Override
  public void interrupt() throws UnableToInterruptJobException {
    System.out.println("interrupted");
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    callable = new Callable() {
      @Override
      public Object call() throws Exception {
        int i = 0;
        boolean flag = true;
        System.out.println("job id" + Thread.currentThread().getId());
        while (flag) {
          i++;
          if(i==10)break;
        }
        System.out.println("i: "+i);
        return i;
      }
    };
    try {
    Future f =new FutureTask(callable);
      System.out.println(f.get());
    } catch (Exception e) {
      e.printStackTrace();
    }
//    Scanner scanner = new Scanner(System.in);
//    System.out.println("please input");
//    String s = scanner.nextLine();
//    try {
//      Thread.sleep(40000);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//    System.out.println(s);

  }
}
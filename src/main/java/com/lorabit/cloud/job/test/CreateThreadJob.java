package com.lorabit.cloud.job.test;

import com.lorabit.cloud.TaskContext;
import com.lorabit.cloud.job.AbstractJob;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lorabit
 * @since 16-2-19
 */
public class CreateThreadJob extends AbstractJob {
  public static ExecutorService exe = Executors.newFixedThreadPool(3);

  @Override
  public void execute(final TaskContext contents) {
    Runnable t = new Runnable() {
      @Override
      public void run() {
        int count = 3;
        while (count-- > 0) {
          try {
            Thread.sleep(7000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.println("hahhhhh");
        }
        System.out.println("ends");
      }
    };
    exe.execute(t);
    System.out.println("execute runnable");
    try {
      Thread.sleep(8000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

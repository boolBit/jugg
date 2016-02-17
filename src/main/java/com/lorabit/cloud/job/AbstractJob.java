package com.lorabit.cloud.job;

import com.lorabit.cloud.TaskContext;

import java.util.concurrent.Callable;

/**
 * @author lorabit
 * @since 16-2-17
 */
public abstract class AbstractJob {

  public abstract void execute(TaskContext contents);

  public void exe(final TaskContext contents) {
    Callable callable = new Callable() {
      @Override
      public Void call() throws Exception {
        execute(contents);
        return null;
      }
    };
    try {
       callable.call();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}

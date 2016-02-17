package com.lorabit.cloud.job;

import com.lorabit.cloud.TaskContext;

import java.util.concurrent.Callable;

/**
 * @author lorabit
 * @since 16-2-17
 */
public abstract class AbstractJob {

  abstract Object execute(TaskContext contents);

  public Object exe(final TaskContext contents) {
    Callable callable = new Callable() {
      @Override
      public Object call() throws Exception {
        return execute(contents);
      }
    };
    try {
      return callable.call();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}

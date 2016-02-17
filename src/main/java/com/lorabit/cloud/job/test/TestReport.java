package com.lorabit.cloud.job.test;

import com.lorabit.cloud.TaskContext;
import com.lorabit.cloud.job.AbstractJob;

/**
 * @author lorabit
 * @since 16-2-17
 */
public class TestReport extends AbstractJob {

  @Override
  public void execute(TaskContext contents) {
    String msg = (String) contents.get(TaskContext.MSG);
    System.out.println("msg coming... " + msg);
  }
}

package com.lorabit.cloud.job.test;

import com.lorabit.cloud.TaskContext;
import com.lorabit.cloud.job.AbstractJob;

/**
 * @author lorabit
 * @since 16-2-17
 */
public class TestJob extends AbstractJob{
  @Override
  public void execute(TaskContext contents) {
    System.out.println(contents.get(TaskContext.DESC));
    System.out.println(contents.get(TaskContext.TASK_NAME));
    System.out.println(contents.get(TaskContext.TYPE));
  }
}

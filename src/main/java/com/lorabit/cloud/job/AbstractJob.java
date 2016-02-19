package com.lorabit.cloud.job;

import com.lorabit.cloud.TaskContext;
import com.lorabit.cloud.status.ExecDetailDO;
import com.lorabit.cloud.status.ExecStatus;
import com.lorabit.cloud.status.RuntimeStatus;
import com.lorabit.cloud.status.dao.ExecDetailDAO;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

/**
 * @author lorabit
 * @since 16-2-17
 */
public abstract class AbstractJob {
  @Resource
  ExecDetailDAO execDetailDAO;

  public abstract void execute(TaskContext contents);

  public void exe(final TaskContext ctx) {
    Callable callable = new Callable() {
      @Override
      public Void call() throws Exception {
        execute(ctx);
        return null;
      }
    };
    ExecDetailDO detail = new ExecDetailDO();
    FutureTask ft = new FutureTask(callable);
    try {
      beforeCallRecord(ctx, detail);
      ft.run();
      if (ctx.getTimeOut() > 0) {
        ft.get(ctx.getTimeOut(), TimeUnit.MILLISECONDS);
      }
      detail.setStatus(ExecStatus.COMPLETED.name());
    } catch (TimeoutException te) {
      detail.setStatus(ExecStatus.TIMEOUT.name());
      detail.setErrors("InterruptedException" + te.getMessage());
      ft.cancel(true);
      te.printStackTrace();
    } catch (InterruptedException e) {
      detail.setStatus(ExecStatus.EXCEPTION.name());
      detail.setErrors("InterruptedException" + e.getMessage());
      e.printStackTrace();
    } catch (ExecutionException e) {
      detail.setStatus(ExecStatus.EXCEPTION.name());
      detail.setErrors("ExecutionException" + e.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      detail.setErrors("Exception" + e.getMessage());
      detail.setStatus(ExecStatus.EXCEPTION.name());
      e.printStackTrace();
    }
    if(ExecStatus.EXCEPTION.name().equals(detail.getStatus())){
      RuntimeStatus.get().error((String) ctx.getTaskName());
    }
    changeStatusToMongo(detail);
    RuntimeStatus.get().leave((String) ctx.getTaskName());
  }

  public void beforeCallRecord(TaskContext ctx, ExecDetailDO detail) {
    detail.setGmtStart(new Date());
    detail.setTaskName((String) ctx.getTaskName());
    detail.setTriggerBy( ctx.getTriggerType().name());
    detail.setStatus(ExecStatus.RUNNING.name());

    RuntimeStatus.get().record(detail); //set last trigger time and trigger count
    storeToMongo(detail);
    RuntimeStatus.get().enter((String) ctx.getTaskName());
  }

  public void storeToMongo(ExecDetailDO detail) {
    execDetailDAO.save(detail);
  }

  public void changeStatusToMongo(ExecDetailDO detail) {
    execDetailDAO.update(detail.getId(), new Date(), detail.getStatus(), detail.getErrors());
  }


}

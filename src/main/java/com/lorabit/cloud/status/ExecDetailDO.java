package com.lorabit.cloud.status;

import com.lorabit.cloud.Task;
import com.lorabit.cloud.TaskContext;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-2-18
 */
@CompoundIndexes({
    @CompoundIndex(name = "task_idx", def = "{'taskName': 1, 'gmtStart': -1}")
})
@Document(collection = "exec_detail")
public class ExecDetailDO {
  @Id
  private ObjectId id;

  private String taskName;
  private String triggerBy;
  private Date gmtStart;
  private Date gmtEnd;
  private String status;
  private String errors;

  public ExecDetailDO() {

  }

  public ExecDetailDO(Map<String, Object> ctx) {
    this.taskName = (String) ctx.get(TaskContext.TASK_NAME);
    this.triggerBy = ((Task.TriggerType) ctx.get(TaskContext.TYPE)).name();
    this.gmtStart = new Date();
    this.status = ExecStatus.UNKNOWN.name();
  }

  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  public String getTriggerBy() {
    return triggerBy;
  }

  public void setTriggerBy(String triggerBy) {
    this.triggerBy = triggerBy;
  }

  public Date getGmtStart() {
    return gmtStart;
  }

  public void setGmtStart(Date gmtStart) {
    this.gmtStart = gmtStart;
  }

  public Date getGmtEnd() {
    return gmtEnd;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setGmtEnd(Date gmtEnd) {
    this.gmtEnd = gmtEnd;
  }

  public String getErrors() {
    return errors;
  }

  public void setErrors(String errors) {
    this.errors = errors;
  }

  public ObjectId getId() {
    return id;
  }
}

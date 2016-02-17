package com.lorabit.model.mongo.test;

import com.google.common.collect.Lists;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author com.lorabit
 * @since 16-2-15
 */
@Document(collection = "test")
public class TestDO implements Serializable {

  private static final long serialVersionUID = 4982658902973591241L;

  @Id
  protected ObjectId id;

  @Indexed
  protected Date createAt;

  protected boolean isEnabled;

  protected List<String> appNames; // 暂时用不到

  @Field
  protected String type;

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public Date getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public void setIsEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  public List<String> getAppNames() {
    return appNames;
  }

  public void setAppNames(List<String> appNames) {
    this.appNames = appNames;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "TestDO{" +
        "id=" + id +
        ", createAt=" + createAt +
        ", isEnabled=" + isEnabled +
        ", appNames=" + appNames +
        ", type='" + type + '\'' +
        '}';
  }

  public static TestDO mock(){
    TestDO testDO = new TestDO();
    testDO.setAppNames(Lists.newArrayList("iphone","android"));
    testDO.setCreateAt(new Date());
    testDO.setIsEnabled(true);
    testDO.setType("phone");
    return testDO;
  }
}

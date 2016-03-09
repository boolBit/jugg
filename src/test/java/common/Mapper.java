package common;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang.math.RandomUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-2-24
 */
public class Mapper {


  public final static org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();

  {
    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
  }


  @Test
  public void test() throws IOException {
    Demo demo =new Demo() ;
//    OutPayInfo info = new OutPayInfo();
//    info.setOutPayAccount("fd");
    System.out.println(JSON.toJSONString(demo));
    System.out.println( mapper.writeValueAsString(demo));
  }

  @Test
  public void t() throws IOException {
//    Demo demo =new Demo() ;
//    System.out.println(mapper.writeValueAsString(demo));
//    Demo demo = mapper.readValue("{}",Demo.class);
//    Demo demo2 = JSONObject.parseObject("", Demo.class);
//    System.out.println(demo);
//    System.out.println(demo2);
    System.out.println(RandomUtils.nextLong());
    System.out.println(5 % 10);
  }

}

class Demo {
  String name;
  List likes;
  Map foods;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public List getLikes() {
    return likes;
  }

  public void setLikes(List likes) {
    this.likes = likes;
  }

  public Map getFoods() {
    return foods;
  }

  public void setFoods(Map foods) {
    this.foods = foods;
  }

  @Override
  public String toString() {
    return "Demo{" +
        "name='" + name + '\'' +
        ", likes=" + likes +
        ", foods=" + foods +
        '}';
  }
}

 class OutPayInfo {

  /**
   * 支付用户外部ID
   */
  private String outPayAccountId;

  /**
   * 支付用户外部账户
   */
  private String outPayAccount;

  /**
   * 外部交易号
   */
  private String outPayNo;

  /**
   * Getter method for property <tt>outPayAccountId</tt>.
   *
   * @return property value of outPayAccountId
   */
  public String getOutPayAccountId() {
    return outPayAccountId;
  }

  /**
   * Setter method for property <tt>outPayAccountId</tt>.
   *
   * @param outPayAccountId value to be assigned to property outPayAccountId
   */
  public void setOutPayAccountId(String outPayAccountId) {
    this.outPayAccountId = outPayAccountId;
  }

  /**
   * Getter method for property <tt>outPayAccount</tt>.
   *
   * @return property value of outPayAccount
   */
  public String getOutPayAccount() {
    return outPayAccount;
  }

  /**
   * Setter method for property <tt>outPayAccount</tt>.
   *
   * @param outPayAccount value to be assigned to property outPayAccount
   */
  public void setOutPayAccount(String outPayAccount) {
    this.outPayAccount = outPayAccount;
  }

  /**
   * Getter method for property <tt>outPayNo</tt>.
   *
   * @return property value of outPayNo
   */
  public String getOutPayNo() {
    return outPayNo;
  }

  /**
   * Setter method for property <tt>outPayNo</tt>.
   *
   * @param outPayNo value to be assigned to property outPayNo
   */
  public void setOutPayNo(String outPayNo) {
    this.outPayNo = outPayNo;
  }

}

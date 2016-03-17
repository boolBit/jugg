package karma;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lorabit
 * @since 16-3-17
 */
public class TestParamType {

  public String say(Map<String, Integer> map, List<Object> list, String s) {
    return "";
  }


  private ObjectMapper mapper = new ObjectMapper();

  @Test
  public void createMsg() throws JsonProcessingException {
    StdMessage sm = new StdMessage();
    for (int i = 0; i < 20; i++) {
      sm.setAct(BuyGlobals.kuaidi100);
      sm.setGmtCreated(System.currentTimeMillis());
      sm.setSubjectId(String.valueOf(RandomUtils.nextInt(400)));
      sm.setSubjectType(EntityType.USER.value);
      sm.setObjectId(String.valueOf(RandomUtils.nextInt(500)));
      sm.setObjectType(EntityType.COUPON.value);
      System.out.println(mapper.writeValueAsString(sm));
    }
  }


  @Test
  public void test() {
    Method[] methods = TestParamType.class.getDeclaredMethods();
    for (Method method : methods) {
      Class[] paramTypes = method.getParameterTypes();
      for (Class c : paramTypes) {
        System.out.println(c);
      }
      Type[] types = method.getGenericParameterTypes();
      for (Type type : types) {
        System.out.println(type);
      }
      Class[][] ptypes = new Class[types.length][];
      for (int ii = 0; ii < types.length; ii++) {
        Type type = types[ii];
        if (type instanceof ParameterizedType) {
          ptypes[ii] = new Class[2];
          Type[] ata = ((ParameterizedType) types[ii]).getActualTypeArguments();
          try {
            String nm1 = ata[0].toString();
            ptypes[ii][0] = Class.forName(nm1.split(" ")[1]);
            if (ata.length > 1) {
              String nm2 = ata[1].toString();
              ptypes[ii][1] = Class.forName(nm2.split(" ")[1]);

            }
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          }
        }
      }
      for (Class[] ptype : ptypes) {
        if (ptype != null)
          for (Class pptype : ptype) {
            System.out.println(pptype);
          }
      }
      System.out.println();
    }
  }


}

@JsonIgnoreProperties(ignoreUnknown = true)
class StdMessage {

  private String act;

  @JsonProperty("sbj_id")
  private String subjectId;

  @JsonProperty("sbj_t")
  private Integer subjectType;

  @JsonProperty("obj_id")
  private String objectId;

  @JsonProperty("obj_t")
  private Integer objectType;

  @JsonProperty("obj_owner")
  private String objectOwner;

  @JsonProperty("gmt_created")
  private Long gmtCreated;

  private Map<String, Object> data;

  @JsonProperty("_v")
  private int version = 0;

  public String getAct() {
    return act;
  }

  public void setAct(String act) {
    this.act = act;
  }

  public String getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(String subjectId) {
    this.subjectId = subjectId;
  }

  public Integer getSubjectType() {
    return subjectType;
  }

  public void setSubjectType(Integer subjectType) {
    this.subjectType = subjectType;
  }

  public String getObjectId() {
    return objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  public Integer getObjectType() {
    return objectType;
  }

  public void setObjectType(Integer objectType) {
    this.objectType = objectType;
  }

  public String getObjectOwner() {
    return objectOwner;
  }

  public void setObjectOwner(String objectOwner) {
    this.objectOwner = objectOwner;
  }

  public Long getGmtCreated() {
    return gmtCreated;
  }

  public void setGmtCreated(Long gmtCreated) {
    this.gmtCreated = gmtCreated;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

}

class BuyGlobals {

  public static final String topic = "buy-event";

  public static final String actAddcart = "addcart";
  public static final String kuaidi100 = "kuaidi100";
  public static final String actCreateOrder = "createOrder";
}

enum EntityType {
  UNKNOWN(-1),
  BLOG(0),
  ALBUM(1),
  USER(2),
  COMMENT(3),
  CLUB(4),
  LETTER(5),
  TOPIC(6),
  TOPIC_COMMENT(7),
  TOPIC_REPLY(8),
  ORDER(9),//商业-订单
  CART(10),//商业-购物车
  INVENTORY(11),//商业-最小商品单元
  COUPON(12),//商业-优惠券
  PROMOTION(13),//商业-优惠活动
  CELLPHONE(14) //手机号
  ;

  public final int value;

  private EntityType(int v) {
    this.value = v;
  }

  public static int toValue(String name) {
    for (EntityType ot : EntityType.values()) {
      if (name.equalsIgnoreCase(ot.name()))
        return ot.value;
    }
    return -1;
  }

  public static EntityType of(Integer i) {
    for (EntityType actionObjType : EntityType.values()) {
      if (Objects.equals(actionObjType.value, i))
        return actionObjType;
    }
    return UNKNOWN;
  }

  public static int toValue(Object name) {
    if (name != null) {
      //1. 直接传了Number
      if (name instanceof Number) {
        return (int) name;
      }
      //2. 传了numberal String
      try {
        return Integer.parseInt(name.toString());
      } catch (NumberFormatException e) {
        //3. 传了非numberal String
        for (EntityType ot : EntityType.values()) {
          if (name.toString().equalsIgnoreCase(ot.name()))
            return ot.value;
        }
      }
    }
    return -1;
  }
}



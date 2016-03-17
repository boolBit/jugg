package cglib;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lorabit.net.nettyserver.telnet.Dog;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nullable;

/**
 * @author lorabit
 * @since 16-3-7
 */
public class EnhancerTest {
ObjectMapper mapper =new ObjectMapper();
  @Test
  public void testCreate() throws JsonProcessingException {
    Dog dog = new Dog();
    dog.bark();
    List<String> list = Lists.newArrayList("1","2","3");
    List<Integer> l =Lists.transform(list, new Function<String, Integer>() {
      @Nullable
      @Override
      public Integer apply(String input) {
        return null;
      }
    });
    System.out.println(mapper.writeValueAsString(l));
    System.out.println(JSON.toJSONString(dog));

  }

  @Test
  public void t(){
    System.out.println(JSON.parseArray("[]", String.class));
  }



}

class Interceptor implements MethodInterceptor {

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    System.out.println("invoke method " + method.getName());
    if(method.getName().equals("say")){
      System.out.println("say say say");
      return null;
    }
//   return method.invoke(obj,args);
    return proxy.invokeSuper(obj, args);
  }
}

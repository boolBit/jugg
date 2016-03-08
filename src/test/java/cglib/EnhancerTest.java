package cglib;

import org.junit.Test;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

import cglib.superc.Dog;

/**
 * @author lorabit
 * @since 16-3-7
 */
public class EnhancerTest {

  @Test
  public void testCreate() {
    Dog dog = (Dog) Enhancer.create(Dog.class, new Interceptor());
    dog.brak();
    dog.say();
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

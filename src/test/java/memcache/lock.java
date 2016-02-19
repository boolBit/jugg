package memcache;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.junit.Test;

import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import spring.BaseSpringContextTests;

/**
 * @author lorabit
 * @since 16-2-19
 */
public class lock extends BaseSpringContextTests {

  @Resource
  XMemcachedClient memcachedClient;

  @Test
  public void lock() {
    try {
      long num = memcachedClient.incr("ClassLearn", 10, 5,500,5);
      System.out.println(num);
      Thread.sleep(3000);
      num = memcachedClient.incr("ClassLearn", 10, 5,500,5);
      System.out.println(num);
      memcachedClient.touch("ClassLearn",10);//re set the expire time
      Thread.sleep(4000);
      String s = memcachedClient.get("ClassLearn");
      System.out.println(s);
    } catch (TimeoutException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (MemcachedException e) {
      e.printStackTrace();
    }
  }
}

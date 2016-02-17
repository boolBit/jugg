package spring.mongo;

import com.lorabit.model.mongo.test.TestDAO;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;

import spring.BaseSpringContextTests;

/**
 * @author com.lorabit
 * @since 16-2-15
 */
public class test  extends BaseSpringContextTests{

  @Resource
  TestDAO testDAO;

  @Test
  public void query() {
    testDAO.query();
  }

  @Test
  public void create() {
    testDAO.create();
  }

  @Test
  public void remove() {
    testDAO.remove();
  }

  @Test
  public void resouce(){
    org.springframework.core.io.Resource r =new ClassPathResource("log4j.xml");
    System.out.println(r.exists());
  }
}

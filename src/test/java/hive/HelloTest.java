package hive;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

import spring.BaseSpringContextTests;

/**
 * @author lorabit
 * @since 16-2-17
 */
public class HelloTest extends BaseSpringContextTests{

  @Resource
  JdbcTemplate hiveJdbcTemplate;

  @Test
  public void test() {
    System.out.println(hiveJdbcTemplate.queryForList("show tables"));
  }



  @Test
  public void testRemovePt(){
    hiveJdbcTemplate.execute("alter table user_action drop partition(pt=200)");
  }
}

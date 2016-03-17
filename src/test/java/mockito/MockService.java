package mockito;

import javax.annotation.Resource;

import lombok.Data;

/**
 * @author lorabit
 * @since 16-3-16
 */
@Data
public class MockService {

  @Resource
  MockDao mockDao;

  public void say(){
    mockDao.say();
  }

}

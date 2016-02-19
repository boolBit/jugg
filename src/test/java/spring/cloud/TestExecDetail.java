package spring.cloud;

import com.lorabit.cloud.status.ExecDetailDO;
import com.lorabit.cloud.status.ExecStatus;
import com.lorabit.cloud.status.dao.ExecDetailDAO;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import spring.BaseSpringContextTests;

/**
 * @author lorabit
 * @since 16-2-19
 */
public class TestExecDetail extends BaseSpringContextTests{

  @Resource
  ExecDetailDAO execDetailDAO;

  @Test
  public void save(){
    ExecDetailDO detail = new ExecDetailDO();
    detail.setGmtStart(new Date());
    detail.setStatus(ExecStatus.RUNNING.name());
    detail.setTaskName("testa");
    execDetailDAO.save(detail);
  }

  @Test
  public void query(){
    List<ExecDetailDO> dos =execDetailDAO.queryByTaskName("test",new DateTime().minusDays(1).toDate
        ());
    System.out.println(dos.size());
  }
}

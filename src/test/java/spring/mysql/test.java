package spring.mysql;

import com.lorabit.dao.CouponDAO;
import com.lorabit.model.mysql.coupon.CouponDO;

import org.junit.Test;

import javax.annotation.Resource;

import spring.BaseSpringContextTests;

/**
 * @author com.lorabit
 * @since 16-2-16
 */
public class test extends BaseSpringContextTests{

  @Resource
  CouponDAO couponDAO;

  @Test
  public void query(){
//    List<CouponDO> coupons =couponDAO.query2();
//    System.out.println(coupons.size());
  }
  @Test
  public void create(){
    couponDAO.save(CouponDO.mock());
  }


}

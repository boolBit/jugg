package com.lorabit.dao;

import com.lorabit.model.mysql.coupon.CouponDO;

import org.apache.ibatis.session.SqlSession;

import javax.annotation.Resource;

/**
 * @author com.lorabit
 * @since 16-2-16
 */
public class CouponDAO {
  @Resource
  SqlSession sqlSessionBuyMaster;

  @Resource
  CouponMap couponMap;

  private String buildMapper(Class mapper, String method) {
    return mapper.getName() + "." + method;
  }

  public int save(CouponDO coupon) {
    return sqlSessionBuyMaster.insert(buildMapper(CouponMap.class, "save"), coupon);
  }


  public int save2(CouponDO coupon) {
    return couponMap.save(coupon);
  }

//  public List<CouponDO> query() {
//    return sqlSessionBuyMaster.selectList(buildMapper(CouponMap.class, "query"), CouponDO.class);
//  }

//  public List<CouponDO> query2() {
////    return couponMap.query();
//  }


}

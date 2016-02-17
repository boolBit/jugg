package com.lorabit.view;

import com.lorabit.dao.CouponDAO;
import com.lorabit.model.mysql.coupon.CouponDO;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @author com.lorabit
 * @since 16-2-14
 */
@Controller
public class HelloController {
  @Resource
  CouponDAO couponDAO;

  @RequestMapping(value = "/")
  @ResponseBody
  public Map index() {
    Map map = new HashMap();
    map.put("key", "value");
    return map;
  }
  @RequestMapping(value = "/coupon")
  @ResponseBody
  public int coupon() {
    return couponDAO.save(CouponDO.mock());
  }


}

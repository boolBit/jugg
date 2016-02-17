package com.lorabit.model.mysql.coupon;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * @author com.lorabit
 * @since 16-2-16
 */
public class CouponDO {

  private int id;
  private int templateId;
  private long uid;
  private long payOrderId;
  private int status;
  private long receivedTs;
  private long expireTs;
  private Date gmtCreate;
  private Date gmtUpdate;

  public CouponDO() {
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

  public long getPayOrderId() {
    return payOrderId;
  }

  public void setPayOrderId(long payOrderId) {
    this.payOrderId = payOrderId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getTemplateId() {
    return templateId;
  }

  public void setTemplateId(int templateId) {
    this.templateId = templateId;
  }

  public long getUid() {
    return uid;
  }

  public void setUid(long uid) {
    this.uid = uid;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public long getReceivedTs() {
    return receivedTs;
  }

  public void setReceivedTs(long receivedTs) {
    this.receivedTs = receivedTs;
  }

  public long getExpireTs() {
    return expireTs;
  }

  public void setExpireTs(long expireTs) {
    this.expireTs = expireTs;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtUpdate() {
    return gmtUpdate;
  }

  public void setGmtUpdate(Date gmtUpdate) {
    this.gmtUpdate = gmtUpdate;
  }

  public static CouponDO mock(){
    CouponDO coupon = new CouponDO();
    coupon.setExpireTs(new Date().getTime());
    coupon.setPayOrderId(123);
    coupon.setReceivedTs(4343);
    coupon.setStatus(1);
    coupon.setTemplateId(3343);
    coupon.setUid(3434l);
    return coupon;
  }
}

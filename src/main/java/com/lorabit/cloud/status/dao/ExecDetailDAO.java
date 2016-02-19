package com.lorabit.cloud.status.dao;

import com.lorabit.cloud.status.ExecDetailDO;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

/**
 * @author lorabit
 * @since 16-2-19
 */
@Repository
public class ExecDetailDAO {

  @Resource
  MongoTemplate cloudTemplate;

  public void save(List<ExecDetailDO> details) {
    cloudTemplate.insert(details);
  }

  public void save(ExecDetailDO detail) {
    cloudTemplate.insert(detail);
  }

  public void update(Object id, Date gmtEnd, String status, String errors) {
    ExecDetailDO detail = cloudTemplate.findOne(new Query(Criteria.where("_id").is(id)),
        ExecDetailDO.class);
    if (detail == null) {
      return;
    }
    detail.setGmtEnd(gmtEnd);
    detail.setStatus(status);
    detail.setErrors(errors);
    cloudTemplate.save(detail);
  }
  public List<ExecDetailDO> queryByTaskName(String tn, Date start) {
    Query query = new Query();
    query.addCriteria(new Criteria("gmtStart").gte(start));
    query.addCriteria(new Criteria("taskName").is(tn));
    query.with(new Sort(Sort.Direction.DESC, "gmtStart"));
    query.limit(100);
    query.maxTime(1, TimeUnit.SECONDS);
    return cloudTemplate.find(query, ExecDetailDO.class);
  }

  public ExecDetailDO find(String name, Date start, Date end) {
    Query query = new Query();
    query.addCriteria(new Criteria("gmtStart").gte(start).lte(end));
    query.addCriteria(new Criteria("taskName").is(name));
    query.with(new Sort(Sort.Direction.DESC, "gmtStart"));
    query.limit(1);
    query.maxTime(3, TimeUnit.SECONDS);
    return cloudTemplate.findOne(query, ExecDetailDO.class);
  }

  public List<ExecDetailDO> queryByNames(List<String> names, Date start, Date end) {
    Query query = new Query();
    query.addCriteria(new Criteria("gmtStart").gt(start).lt(end));
    query.addCriteria(new Criteria("taskName").in(names));
    query.with(new Sort(Sort.Direction.DESC, "gmtStart"));
    query.maxTime(3, TimeUnit.SECONDS);
    return cloudTemplate.find(query, ExecDetailDO.class);
  }
}

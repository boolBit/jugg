package com.lorabit.model.mongo.test;

import com.mongodb.WriteResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author com.lorabit
 * @since 16-2-15
 */
public class TestDAO {

  @Autowired
  @Qualifier(value = "testdb")
  MongoTemplate mongoTemplate;

  public void query() {
    List<Criteria> conditions = new ArrayList<>();
    conditions.add(Criteria.where("isEnabled").is(true));
    Query query = new Query(
        new Criteria().andOperator(conditions.toArray(new Criteria[conditions.size()])));
    query.with(new Sort(Sort.Direction.DESC, "createAt"));

    List<TestDO> tests = mongoTemplate.find(query, TestDO.class);
    for (TestDO test : tests) {
      System.out.println(test);
    }
  }

  public void create() {
    TestDO testDo = TestDO.mock();
//    mongoTemplate.save(testDo);
    mongoTemplate.insert(testDo);
    System.out.println(testDo.getId());
  }

  public void remove(){
    Query query = new Query(Criteria.where("_id").is("56c1a97f4d425c4a6ba457c4"));
    WriteResult res = mongoTemplate.remove(query, TestDO.class);
    System.out.println(res.getN());
  }
}

package queue;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import org.junit.Test;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author lorabit
 * @since 16-3-1
 */
public class T {
  @Test
  public void t() {
    ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
    queue.add("hah");
    Object o = queue.poll();
    queue.isEmpty();
    System.out.println(o);
    System.out.println(queue.size());
    System.out.println("end");
    System.out.println("中文".getBytes().length);
    int s = 10 * 1024 / 3;
    System.out.println(s);
  }

  @Test
  public void testRemoveIterator() {
    List<String> strs = Lists.newArrayList("a", "b", "c");
    Iterator<String> it = strs.iterator();
    while (it.hasNext()) {
      String s = it.next();
      it.remove();
    }

    System.out.println(strs.size());
  }

  @Test
  public void setToList() {
    Map<String, Object> map = new HashMap<>();
    map.put("1", "2");
    map.put("2", "3");

    final List<Integer> ids = Lists.transform(Lists.newArrayList(map.keySet()), new Function<String, Integer>
        () {
      @Override
      public Integer apply(String id) {
        return Integer.valueOf(id) / 2 == 0 ? null : Integer.valueOf(id) / 2;
      }
    });

    System.out.println(ids);
  }

    float RATE = 100F;
  @Test
  public void listToSet() throws ParseException {
    double res;
    DecimalFormat df = new DecimalFormat("######0.00");
    for (long fee = 8000L; fee < 8040; fee++) {
         res = fee / RATE;
      if (fee % RATE == 0) {
        System.out.println((int)res);
      }else{
        System.out.println(df.parse(df.format(res)));

      }

    }
  }

  @Test
  public void tee(){
    long price = 300;
    Float p =price / RATE;
    System.out.println(p);
  }

  @Test
  public void testTime(){
    System.out.println(System.currentTimeMillis());
    System.out.println(currTimeSeq());
    System.out.println(currTimeSeq());
    System.out.println(currTimeSeq());
  }

  private int currTimeSeq() {
    long t = System.currentTimeMillis();
    long x = t - (t / 10000) * 10000;
    return (int) (x / 500.0);
  }
}

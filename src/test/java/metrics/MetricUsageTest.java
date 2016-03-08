package metrics;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;
import com.codahale.metrics.Timer;
import com.codahale.metrics.json.MetricsModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lorabit.metrics.metricdomain.KafkaMetric;
import com.lorabit.util.HttpHelper;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author lorabit
 * @since 16-2-23
 */
public class MetricUsageTest {
  public final static ObjectMapper mapper = new ObjectMapper().registerModule(new MetricsModule(
      TimeUnit.SECONDS, // rate unit
      TimeUnit.SECONDS, // duration unit
      false, // show samples
      MetricFilter.ALL));
  public static final ObjectMapper MAPPER = new ObjectMapper();
  public static final MetricRegistry registry = new MetricRegistry();

  private static final String DEMO_KAIROSDB_PUT_URL = "http://localhost:8080/api/v1/datapoints";

  private static final HttpHelper httpHelper = new HttpHelper();

  static {
    MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//    registry.register("jvm.buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
//    registry.register("jvm.classes", new ClassLoadingGaugeSet());
//    registry.register("jvm.memory", new MemoryUsageGaugeSet());
//    registry.register("jvm.gc", new GarbageCollectorMetricSet());
//    registry.register("jvm.threads", new ThreadStatesGaugeSet());
  }

  @Test
  public void now() {
    System.out.println(new Date().getTime());
  }

  @Test
  public void total() throws IOException {
    final List list = Lists.newArrayList("1", "2");
    registry.register(MetricRegistry.name(MetricUsageTest.class, "gauges"), new Gauge<Integer>() {
      @Override
      public Integer getValue() {
        return list.size();
      }
    });
    Counter evictions = registry.counter("counter");
    evictions.inc();
    evictions.inc(3);
    evictions.dec();
    evictions.dec(2);
    final Histogram resultCounts = registry.histogram("histograms");
    resultCounts.update(12);
    resultCounts.update(14);
    resultCounts.update(16);
    final Meter rate = registry.meter("rate");
    final Meter rate2 = registry.meter("rate2");
    rate.mark();
    rate.mark(4);
    rate2.mark(2);
    final Timer timer = registry.timer("timer");
    int cnt = 3;
    while (cnt-- > 0) {
      Timer.Context context = null;
      try {
        context = timer.time();
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        context.stop();
      }
    }
    String s = mapper.writeValueAsString(registry);
    KafkaMetric km = MAPPER.readValue(s, KafkaMetric.class);
    System.out.println(km);
  }

  @Test
  public void gauges() {
    final List list = Lists.newArrayList("1", "2");
    registry.register(MetricRegistry.name(MetricUsageTest.class, "gauges"), new Gauge<Integer>() {
      @Override
      public Integer getValue() {
        return list.size();
      }
    });
    output();
  }

  @Test
  public void counter() {
    Counter evictions = registry.counter("counter");
    evictions.inc();
    evictions.inc(3);
    evictions.dec();
    evictions.dec(2);
    output();
  }

  @Test
  public void histograms() {
    final Histogram resultCounts = registry.histogram("histograms");
    resultCounts.update(12);
    resultCounts.update(14);
    resultCounts.update(16);
    output();
  }

  @Test
  public void meters() {
    final Meter rate = registry.meter("rate");
    rate.mark();
    rate.mark(4);
    output();
  }

  @Test
  public void timer() {
    final Timer timer = registry.timer("timer");
    int cnt = 3;
    while (cnt-- > 0) {
      Timer.Context context = null;
      try {
        context = timer.time();
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        context.stop();
      }
    }
    output();
  }


  @Test
  public void logToKairosDB() throws JsonProcessingException {
    final Meter rate = registry.meter("rate");
    rate.mark();
    rate.mark(4);
    System.out.println(mapper.writeValueAsString(registry));
    httpHelper.postReq(new HttpPost(DEMO_KAIROSDB_PUT_URL), mapper.writeValueAsString(registry));
  }


  public void output() {
    try {
      System.out.println(mapper
          .writerWithDefaultPrettyPrinter()
          .writeValueAsString(registry));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public Map testSendToDB(String input) {
    return null;
  }

  @Test
  public void testPutToDB() throws JsonProcessingException, InterruptedException {
    List<Map> toAdd = new ArrayList<>();
    int count = 5;
    while (count-- > 0) {
      Map map = new HashMap();
      Map tag = Maps.newHashMap();
      tag.put("host", "127.0.0.1");
      map.put("name", "lorabit");
      map.put("timestamp", new Date().getTime());
      map.put("tags", tag);
      map.put("value", RandomUtils.nextDouble(new Random()));
      toAdd.add(map);
      Thread.sleep(2000);
    }
    System.out.println(mapper.writeValueAsString(toAdd));
    httpHelper.postReq(new HttpPost(DEMO_KAIROSDB_PUT_URL), mapper.writeValueAsString(toAdd));
  }

}

class CacheHitRatio extends RatioGauge {
  private final Meter hits;
  private final Timer calls;

  public CacheHitRatio(Meter hits, Timer calls) {
    this.hits = hits;
    this.calls = calls;
  }

  @Override
  public Ratio getRatio() {
    return Ratio.of(hits.getOneMinuteRate(),
        calls.getOneMinuteRate());
  }
}



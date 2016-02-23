package metrics;

import com.google.common.collect.Lists;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.util.List;
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
  public static final MetricRegistry registry = new MetricRegistry();

  static {
//    registry.register("jvm.buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
//    registry.register("jvm.classes", new ClassLoadingGaugeSet());
//    registry.register("jvm.memory", new MemoryUsageGaugeSet());
//    registry.register("jvm.gc", new GarbageCollectorMetricSet());
//    registry.register("jvm.threads", new ThreadStatesGaugeSet());
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


  public void output() {
    try {
      System.out.println(mapper
          .writerWithDefaultPrettyPrinter()
          .writeValueAsString(registry));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
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



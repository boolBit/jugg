package com.lorabit.metrics;

import com.lorabit.metrics.report.Reporter;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

/**
 * @author lorabit
 * @since 16-2-25
 */
public class MetricCenter implements ApplicationListener {
  List<Reporter> reporterList;
  public static final long INTEVAL = 5000L;

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
    if (event instanceof ContextRefreshedEvent) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          while (true) {
            try {
              Thread.sleep(INTEVAL);

            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      }, "metric report demon thread").start();
    }
  }
}

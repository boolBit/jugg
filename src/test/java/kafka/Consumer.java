package kafka;

import com.google.common.collect.Maps;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * @author lorabit
 * @since 16-2-17
 */
public class Consumer {
  private final Map<String, Integer> topicCountMap = Maps.newHashMap();
  private Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap;
  private ConsumerConnector consumer;
  private String topic;

  public static void main(String[] argvs) {
    Producer pro = new Producer();
    pro.init();
    pro.send("word","fdfdfdsf");
    Consumer listener = new Consumer();
    listener.topic = "word";
    listener.listen();
  }

  private ConsumerConfig createConsumerConfig() {
    String mxName = ManagementFactory.getRuntimeMXBean().getName();//pid@hostname//mxName.replace('@', '-')
    String consumerId = String.format("%s-%s", "test", "lorabit");
    Properties props = new Properties();
    props.put("zookeeper.connect", "localhost:2181");
    props.put("group.id", "test");
    props.put("zookeeper.session.timeout.ms", "8000");
    props.put("zookeeper.sync.time.ms", "500");
    props.put("auto.commit.interval.ms", "1000");
    props.put("refresh.leader.backoff.ms", "10000");
    props.put("consumer.id", consumerId);
    return new ConsumerConfig(props);
  }

  public void listen() {
    consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());
    topicCountMap.put(topic, new Integer(1));
    consumerMap = consumer.createMessageStreams(topicCountMap);
    KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
    ConsumerIterator<byte[], byte[]> it = stream.iterator();
    while (it.hasNext()) {
      String msg = new String(it.next().message());
      System.out.println(msg);
      if("exit".equals(msg)) break;
    }
    System.out.println("consumer shutdown");
    consumer.shutdown();
  }
}

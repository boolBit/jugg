package kafka;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

import java.util.Properties;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * @author lorabit
 * @since 16-2-17
 */
public class Producer {

    private final Logger log = Logger.getLogger("main");

    private final ObjectMapper mapper = new ObjectMapper();

    private kafka.javaapi.producer.Producer<String, String> producer;

    private String batchNumMessages = "200";
    private String producerType = "sync";
    private String requestRequiredAcks = "0";

    protected Properties prepareKafkaParams() {
      Properties props = new Properties();
      props.put("metadata.broker.list", "localhost:9092");
      props.put("serializer.class", "kafka.serializer.StringEncoder");
      props.put("request.required.acks", "1");
      props.put("producer.type", producerType);
      props.put("queue.enqueue.timeout.ms", "-1");
      props.put("batch.num.messages", batchNumMessages);
      props.put("compression.codec", "1");
      return props;
    }

    public void init() {
      producer = new kafka.javaapi.producer.Producer<String, String>(new ProducerConfig(prepareKafkaParams()));
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          if (producer != null) producer.close();
        }
      });
    }

    public void send (String topic, String msg) {
        producer.send(new KeyedMessage<String, String>(
            topic, String.valueOf(Math.random()), msg
        ));
    }

    public String getBatchNumMessages() {
      return batchNumMessages;
    }

    public void setBatchNumMessages(String batchNumMessages) {
      this.batchNumMessages = batchNumMessages;
    }

    public String getProducerType() {
      return producerType;
    }

    public void setProducerType(String producerType) {
      this.producerType = producerType;
    }

    public String getRequestRequiredAcks() {
      return requestRequiredAcks;
    }

    public void setRequestRequiredAcks(String requestRequiredAcks) {
      this.requestRequiredAcks = requestRequiredAcks;
    }
  }

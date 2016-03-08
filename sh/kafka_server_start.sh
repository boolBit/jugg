#!/bin/sh

/home/hellokitty/open/yunjisuan/kafka_2.11-0.9.0.0/bin/zookeeper-server-start.sh /home/hellokitty/open/yunjisuan/kafka_2.11-0.9.0.0/config/zookeeper.properties &
/home/hellokitty/open/yunjisuan/kafka_2.11-0.9.0.0/bin/kafka-server-start.sh /home/hellokitty/open/yunjisuan/kafka_2.11-0.9.0.0/config/server.properties &
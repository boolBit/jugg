#!/bin/sh


redis-server &

/home/hellokitty/open/yunjisuan/hadoop/sbin/start-dfs.sh &

/home/hellokitty/open/yunjisuan/hive/bin/hive --service metastore  &
/home/hellokitty/open/yunjisuan/hive/bin/hiveserver2 &

/home/hellokitty/open/yunjisuan/kafka_2.11-0.9.0.0/bin/zookeeper-server-start.sh /home/hellokitty/open/yunjisuan/kafka_2.11-0.9.0.0/config/zookeeper.properties &
/home/hellokitty/open/yunjisuan/kafka_2.11-0.9.0.0/bin/kafka-server-start.sh /home/hellokitty/open/yunjisuan/kafka_2.11-0.9.0.0/config/server.properties &

/home/hellokitty/open/solr/solr/bin/solr start -c -s /home/hellokitty/open/solr/solr/google


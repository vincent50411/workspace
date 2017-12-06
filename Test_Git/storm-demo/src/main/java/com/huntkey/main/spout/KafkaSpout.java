package com.huntkey.main.spout;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;

import java.util.Map;
import java.util.Properties;

/**
 * Created by liuwens on 2017/9/26.
 */
public class KafkaSpout implements IRichSpout
{
    private static final String TOPIC = "spark_streaming_kafka_test"; //kafka创建的topic
    private static final String CONTENT = "This is a single message"; //要发送的内容
    private static final String BROKER_LIST = "vm-linux-163.hkhf.hkgp.net:6667,vm-linux-164.hkhf.hkgp.net:6667,vm-linux-165.hkhf.hkgp.net:6667"; //broker的地址和端口
    private static final String SERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer"; // 序列化类
    private static final String KEY_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.IntegerSerializer"; // 序列化类

    private SpoutOutputCollector spoutOutputCollector;


    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector)
    {
        this.spoutOutputCollector = spoutOutputCollector;

    }

    public void close() {

    }

    public void activate() {

    }

    public void deactivate() {

    }

    public void nextTuple() {

    }

    public void ack(Object o) {

    }

    public void fail(Object o) {

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}

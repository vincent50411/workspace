package com.huntkey.main.bolt.kafka;

import com.alibaba.fastjson.JSONObject;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;

import java.util.Map;

/**
 * Created by liuwens on 2017/9/30.
 */
public class TransferBolt extends BaseRichBolt{

    private OutputCollector collector;

    public void prepare(Map map, TopologyContext context, OutputCollector collector){
        this.collector = collector;
    }

    public void execute(Tuple input){
        String line = input.getString(0);
        JSONObject json = JSONObject.parseObject(line);

        System.out.println("*****************************************************************");

        System.out.println("--> json:" + json);

        System.out.println("*****************************************************************");


    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}

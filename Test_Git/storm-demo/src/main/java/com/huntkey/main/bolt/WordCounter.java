package com.huntkey.main.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuwens on 2017/9/5.
 */
public class WordCounter extends BaseRichBolt
{
    Integer id;
    String name;
    Map<String,Integer> counters;
    private OutputCollector collector;

    /**
     * 如果没有声明任何输出，即declareOutputFields方法返回的是null，则prepare方法不会执行
     * @param map
     * @param topologyContext
     * @param outputCollector
     */
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.counters = new HashMap<String, Integer>();
        this.collector = outputCollector;
        this.name = topologyContext.getThisComponentId();
        this.id = topologyContext.getThisTaskId();
    }

    public void execute(Tuple tuple)
    {
        System.out.println( "WordCounter.execute()" );
        String str = tuple.getString(0);
        /**
         * 如果单词尚不存在于map，我们就创建一个，如果已在，我们就为它加1
         */
        if(!counters .containsKey(str))
        {
            counters.put(str ,1);
        }
        else
            {
            Integer c = counters.get(str ) + 1;
            counters.put(str ,c );
        }
        //对元组作为应答
        collector.ack(tuple);
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        System. out.println("WordCounter.declareOutputFields()" );
    }

    @Override
    public void cleanup()
    {
        for(Map.Entry<String,Integer> entry : counters.entrySet())
        {
            System. out.println(entry .getKey()+": "+ entry.getValue());
        }

        System.out.println( "WordCounter.cleanup()" );
    }
}

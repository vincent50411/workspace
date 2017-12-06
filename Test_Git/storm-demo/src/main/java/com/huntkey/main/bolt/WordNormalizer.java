package com.huntkey.main.bolt;

import org.apache.storm.shade.org.apache.commons.lang.StringUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * Created by liuwens on 2017/9/5.
 */
public class WordNormalizer extends BaseRichBolt
{
    private static final long serialVersionUID = 3644849073824009317L;
    private OutputCollector collector ;

    /**
     * 如果没有声明任何输出，即declareOutputFields方法返回的是null，则prepare方法不会执行
     * @param map
     * @param topologyContext
     * @param outputCollector
     */
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        System. out.println("WordNormalizer.prepare()" );
        this. collector = outputCollector ;

        int taskCounter = topologyContext.getComponentTasks("word-normalizer").size();

        System. out.println(String.format("-------------------------------------- task count: %d -------------------------------", taskCounter) );
    }

    public void execute(Tuple tuple) {
        System.out.println( "WordNormalizer.execute()" );
        String sentence = tuple.getString(0);
        String[] words = sentence .split(" ");
        for(String word : words){
            word = word .trim();

            if(!StringUtils.isBlank(word))
            {
                word = word .toLowerCase();
                /*//发布这个单词*/
                collector.emit(tuple ,new Values(word));
            }
        }
        //对元组做出应答
        collector.ack(tuple);
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        System. out.println("WordNormalizer.declareOutputFields()" );
        outputFieldsDeclarer.declare(new Fields("word"));
    }
}

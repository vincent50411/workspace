package com.huntkey.main.bolt;

import com.sun.javafx.binding.StringFormatter;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by liuwens on 2017/10/11.
 */
public class PrintLineBolt extends BaseRichBolt
{
    private Logger LOG =LoggerFactory.getLogger(PrintLineBolt.class);

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector)
    {

    }

    public void execute(Tuple tuple)
    {
        String lineMessage = null;

        String sourceCompent = tuple.getSourceComponent();

        if(tuple.contains("line"))
        {
            lineMessage = tuple.getStringByField("line");
        }

        String wordMessage = null;

        if(tuple.contains("word"))
        {
            wordMessage = tuple.getStringByField("word");
        }

        LOG.info(String.format("--> Source Coment:%s; line message %s  ; wordMessage %s", sourceCompent, lineMessage, wordMessage));
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer)
    {

    }
}

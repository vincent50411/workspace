package com.huntkey.main.spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.*;
import java.util.Map;

/**
 * Created by liuwens on 2017/9/5.
 */
public class WordReader extends BaseRichSpout
{
    private SpoutOutputCollector collector ;
    private FileReader fileReader ;
    BufferedReader reader;
    private boolean completed = false;

    public void open(Map conf, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector)
    {
        System.out.println( "WordReader.open(Map conf, TopologyContext context, SpoutOutputCollector collector)");
        String fileName = conf .get("fileName").toString();
        InputStream inputStream = WordReader.class.getClassLoader().getResourceAsStream(fileName);

        try
        {
            FileInputStream fileInputStream = new FileInputStream(new File(fileName));

            reader = new BufferedReader(new InputStreamReader(fileInputStream));
            this.collector = spoutOutputCollector;

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }



    }

    public void nextTuple()
    {
        /**
         * 这个方法会不断的被调用，直到整个文件都读完了，我们将等待并返回。
         */
        if (completed ) {
            try {
                Thread. sleep(1000);
            } catch (InterruptedException e ) {
                // 什么也不做
            }
            return;
        }
        String str;

        try {
            int i = 0;
            // 读所有文本行
            while ((str = reader.readLine()) != null) {
                System. out.println("WordReader.nextTuple(),emits time:" + i++);
                /**
                 * 按行发布一个新值
                 */
                this.collector .emit(new Values( str), str );
            }
        } catch (Exception e ) {
            throw new RuntimeException("Error reading tuple", e);
        } finally {
            completed = true ;
        }
    }

    /**
     * 定义输出到stream中的Tuple中的数据格式，如果emit中有多个值，则Fields需要定义多个，数量上要保持一致
     * @param outputFieldsDeclarer
     */
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer)
    {
        System. out.println("WordReader.declareOutputFields(OutputFieldsDeclarer declarer)");
        outputFieldsDeclarer.declare(new Fields("line"));
    }
}

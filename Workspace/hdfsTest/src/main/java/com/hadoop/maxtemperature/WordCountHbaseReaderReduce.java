package com.hadoop.maxtemperature;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by liuwens on 2017/7/18.
 */
public class WordCountHbaseReaderReduce extends Reducer<Text,Text,Text,Text> {
    private Text result = new Text();
    private static final String UTF_8 = "UTF-8";

    @Override
    protected void reduce(Text key, Iterable<Text> values,Context context)
            throws IOException, InterruptedException {
        for(Text val:values)
        {
            result.set(val.toString().getBytes(UTF_8));
            context.write(key, result);
        }
    }
}

package com.huntkey.controller;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.io.crypto.Context;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by liuwens on 2017/7/11.
 */
@RestController
@RequestMapping("/map")
public class MapReduceTestController
{

    static {
        System.setProperty("hadoop.home.dir","D:\\HuntKey\\Hadoop\\Hadoop\\hadoop-2.6.5\\hadoop-2.6.5" );
    }

    @RequestMapping("/test")
    public void test()
    {
        // 取得一个数据库连接的配置参数对象
        Configuration conf = HBaseConfiguration.create();

        // 设置连接参数：HBase数据库所在的主机IP vm-centeros03.hkgp.net,vm-centeros02.hkgp.net, vm-centeros01.hkgp.net
        conf.set("hbase.zookeeper.quorum", "vm-centeros03.hkgp.net,vm-centeros02.hkgp.net,vm-centeros01.hkgp.net");
        // 设置连接参数：HBase数据库使用的端口
        conf.set("hbase.zookeeper.property.clientPort", "2181");

        conf.set("zookeeper.znode.parent", "/hbase-unsecure");

        //System.setProperty("hadoop.home.dir","D:\\HuntKey\\Hadoop\\Hadoop\\hadoop-2.6.5\\hadoop-2.6.5" );

        Job job;
        try
        {
            job = Job.getInstance(conf,"wordstat");
            job.setJarByClass(MapReduceTestController.class);

            Scan scan = new Scan();
            //指定要查询的列族
            //scan.addColumn(Bytes.toBytes("address"),null);

            //指定Mapper读取的表为word
            TableMapReduceUtil.initTableMapperJob("t_hk_sales", scan, MyMapper.class, Text.class, Text.class, job);

            //设置任务数据的输出路径；
            FileOutputFormat.setOutputPath(job, new Path("/user/root/output_" + new Date().getTime() + "_test"));
            job.setReducerClass(WordCountHbaseReaderReduce.class);

            //指定Reducer写入的表为stat
            //TableMapReduceUtil.initTableReducerJob("member_test_stat", MyReducer.class, job);

            //System.exit(job.waitForCompletion(true)?0:1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception cfe)
        {
            cfe.printStackTrace();
        }

    }

    /**
     * TableMapper<Text,IntWritable>  Text:输出的key类型，IntWritable：输出的value类型
     */
    public static class MyMapper extends TableMapper<Text,Text> {

        private static IntWritable one = new IntWritable(1);
        private static Text word = new Text();

        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException
        {
            //表里面只有一个列族，所以我就直接获取每一行的值
            String words = Bytes.toString(value.list().get(0).getValue());

            System.out.println("--> words:" + words);

            StringTokenizer st = new StringTokenizer(words);
            while (st.hasMoreTokens())
            {
                String s = st.nextToken();
                word.set(s);
                context.write(word, new Text("sdfsdfdsfdsfdsfdsf"));
            }
        }
    }

    /**
     * TableReducer<Text,IntWritable>  Text:输入的key类型，IntWritable：输入的value类型，ImmutableBytesWritable：输出类型
     */
    public static class WordCountHbaseReaderReduce extends Reducer<Text,Text,Text,Text> {
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
}

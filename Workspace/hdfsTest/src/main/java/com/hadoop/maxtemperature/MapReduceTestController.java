package com.hadoop.maxtemperature;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.*;

/**
 * Created by liuwens on 2017/7/11.
 */

public class MapReduceTestController
{

    public static void main(String[] args)
    {
        // 取得一个数据库连接的配置参数对象
        Configuration conf = HBaseConfiguration.create();

        // 设置连接参数：HBase数据库所在的主机IP vm-centeros03.hkgp.net,vm-centeros02.hkgp.net, vm-centeros01.hkgp.net
        conf.set("hbase.zookeeper.quorum", "vm-centeros03.hkgp.net,vm-centeros02.hkgp.net,vm-centeros01.hkgp.net");
        // 设置连接参数：HBase数据库使用的端口
        conf.set("hbase.zookeeper.property.clientPort", "2181");

       conf.set("zookeeper.znode.parent", "/hbase-unsecure");

       //本地环境调试
        //System.setProperty("hadoop.home.dir","D:\\HuntKey\\Hadoop\\Hadoop\\hadoop-2.6.5\\hadoop-2.6.5" );

        Job job;
        try
        {
            List scans = new ArrayList();


            job = Job.getInstance(conf,"wordstat");
            job.setJarByClass(MapReduceTestController.class);

            Scan scan_01 = new Scan();

            //指定要查询的列族
            //scan.addColumn(Bytes.toBytes("address"),null);

            //指定Mapper读取的表为word
            TableMapReduceUtil.initTableMapperJob("t_hk_dept", scan_01, MyMapper.class, Text.class, Text.class, job);

            //设置任务数据的输出路径；
            Long index = new Date().getTime();
            FileOutputFormat.setOutputPath(job, new Path("/user/root/output_" + index + "kylin_cal_dt"));
            job.setReducerClass(WordCountHbaseReaderReduce.class);

            //指定Reducer写入的表为stat
            //TableMapReduceUtil.initTableReducerJob("member_test_stat", MyReducer.class, job);

            System.exit(job.waitForCompletion(true)?0:1);
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
        protected void map(ImmutableBytesWritable key, Result result, Mapper.Context context) throws IOException, InterruptedException
        {
            StringBuffer sb = new StringBuffer("");
            for(Map.Entry<byte[],byte[]> entry:result.getFamilyMap("cf".getBytes()).entrySet())
            {
                String str =  new String(entry.getValue());
                //将字节数组转换为String类型
                if(str != null){
                    sb.append(new String(entry.getValue()));
                    sb.append("|");
                    //sb.append(str);
                }

                context.write(new Text(new String(key.get())), new Text(new String(sb)));
            }
        }
    }

    /**
     * TableReducer<Text,IntWritable>  Text:输入的key类型，IntWritable：输入的value类型，ImmutableBytesWritable：输出类型
     */
    public static class MyReducer extends TableReducer<Text,IntWritable,ImmutableBytesWritable>
    {
        protected void reduce(Text key, Iterable<IntWritable> values, Reducer.Context context) throws IOException, InterruptedException
        {
            int sum = 0;
            for(IntWritable val:values)
            {
                sum += val.get();
            }
            //添加一行记录，每一个单词作为行键
            Put put = new Put(Bytes.toBytes(key.toString()));
            //在列族result中添加一个标识符num,赋值为每个单词出现的次数
            //String.valueOf(sum)先将数字转化为字符串，否则存到数据库后会变成\x00\x00\x00\x这种形式
            //然后再转二进制存到hbase。
            put.add(Bytes.toBytes("info"), Bytes.toBytes("num"), Bytes.toBytes(String.valueOf(sum)));

            context.write(new ImmutableBytesWritable(Bytes.toBytes(key.toString())), put);
        }
    }
}

package com.hadoop.maxtemperature;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapred.TableMap;
import org.apache.hadoop.hbase.mapreduce.MultiTableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwens on 2017/7/24.
 */
public class MapReduceMultTableMain
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

        //定义job
        Job job;
        try
        {
            job = Job.getInstance(conf,"test_mp_001");
            job.setJarByClass(MapReduceMultTableMain.class);


            List scans = new ArrayList();

            for (int argIndex = 0; argIndex < args.length; argIndex++)
            {
                TableName tableName = TableName.valueOf(args[argIndex]);
                Scan scan_001 = new Scan();
                scan_001.setAttribute(Scan.SCAN_ATTRIBUTES_TABLE_NAME, tableName.getName());

                scans.add(scan_001);

            }

            TableMapReduceUtil.initTableMapperJob(scans, MultMapper.class, Text.class, Text.class, job);

            //设置任务数据的输出路径；
            Long index = new Date().getTime();
            FileOutputFormat.setOutputPath(job, new Path("/user/root/output_test_mp_001_" + index));
            job.setReducerClass(WordCountHbaseReaderReduce.class);

            System.exit(job.waitForCompletion(true)?0:1);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    public static class MultMapper extends TableMapper<Text, Text>
    {
        @Override
        protected void map(ImmutableBytesWritable key, Result result, Mapper.Context context) throws IOException, InterruptedException
        {
            StringBuffer sb = new StringBuffer("");

            String[] familys = new String[]{"cf", "address", "info"};

            for(int index = 0; index < familys.length; index++)
            {
                String columnFamilyName = familys[index];
                for(Map.Entry<byte[],byte[]> entry:result.getFamilyMap(columnFamilyName.getBytes()).entrySet())
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


    }



}

package com.hunkey.controller;

import com.alibaba.fastjson.JSON;
import com.hunkey.dto.KPIRequestParamDto;
import kafka.producer.KeyedMessage;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Created by liuwens on 2017/9/25.
 */
@RestController
@RequestMapping("/kpi")
public class KpiServiceController
{

    private static final String TOPIC = "spark_streaming_kafka_test"; //kafka创建的topic
    private static final String CONTENT = "This is a single message"; //要发送的内容
    private static final String BROKER_LIST = "vm-linux-163.hkhf.hkgp.net:6667,vm-linux-164.hkhf.hkgp.net:6667,vm-linux-165.hkhf.hkgp.net:6667"; //broker的地址和端口
    private static final String SERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer"; // 序列化类
    private static final String KEY_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.IntegerSerializer"; // 序列化类

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public void sendMessageToKafka(@RequestBody KPIRequestParamDto kpiRequestParamDto)
    {

        System.out.println("--> kpiRequestParamDto:" + JSON.toJSONString(kpiRequestParamDto));


        Properties props = new Properties();
        props.put("bootstrap.servers", BROKER_LIST);
        props.put("key.serializer", KEY_SERIALIZER_CLASS);
        props.put("value.serializer", SERIALIZER_CLASS);
        props.put("producer.type", "sync");
        props.put("queue.enqueue.timeout.ms", "20000");

        Producer<Integer, String> producer = new KafkaProducer<Integer, String>(props);

        int messageNo = 1;

        while(messageNo < 2)
        {
            //Send one message.
            String messageStr = "Message_" + messageNo;
            long startTime = System.currentTimeMillis();
            try
            {
                producer.send(new ProducerRecord<Integer, String>(TOPIC, messageNo, CONTENT), new DemoCallBack(startTime, messageNo, CONTENT)).get();


                //轮训redis，查看公式计算结果


            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }

            System.out.println("Sent message: (" + messageNo + ", " + messageStr + ")");

            ++messageNo;
        }


    }

    class DemoCallBack implements Callback {

        private final long startTime;
        private final int key;
        private final String message;

        public DemoCallBack(long startTime, int key, String message)
        {
            this.startTime = startTime;
            this.key = key;
            this.message = message;
        }

        /**
         * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
         *                  occurred.
         * @param exception The exception thrown during processing of this record. Null if no error occurred.
         */
        public void onCompletion(RecordMetadata metadata, Exception exception)
        {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (metadata != null)
            {
                System.out.println(
                        "message(" + key + ", " + message + ") sent to partition(" + metadata.partition() +
                                "), " +
                                "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
            } else {
                exception.printStackTrace();
            }
        }


    }


}

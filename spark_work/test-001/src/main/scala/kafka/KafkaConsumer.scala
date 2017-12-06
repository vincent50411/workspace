package kafka

import kafka.serializer.StringDecoder
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by liuwens on 2017/9/18.
  */
object KafkaConsumer
{


  def main(args:Array[String]):Unit =
  {
    val sprakConf = new SparkConf().setAppName("DirectKafkaWordCountDemo")
    //此处在idea中运行时请保证local[2]核心数大于2
    sprakConf.setMaster("local[2]")

    val ssc = new StreamingContext(sprakConf, Seconds(3))


    val brokers = "vm-linux-163.hkhf.hkgp.net:6667,vm-linux-164.hkhf.hkgp.net:6667,vm-linux-165.hkhf.hkgp.net:6667";
    val topics = Array("spark_streaming_kafka_test");

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val messagesStream = KafkaUtils.createDirectStream[String, String](ssc,
      LocationStrategies.PreferConsistent,  ConsumerStrategies.Subscribe[String, String](topics, kafkaParams))

    //map是spark的tranformation，最后一步产生的那个RDD必须有相应Action操作，否则会出错
    messagesStream.map(message => (message.key(), message.value()));

    messagesStream.map(record =>
      {
        println("-------------------------------")
        (record.key, record.value);
        println("recive value:" + record.value)
        println("-------------------------------")
      }).print();

    ssc.start()

    ssc.awaitTermination()

  }



}

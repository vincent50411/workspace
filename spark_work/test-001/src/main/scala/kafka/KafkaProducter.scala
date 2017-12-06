package kafka

import java.util.Properties

import com.alibaba.fastjson.JSONObject
import kafka.producer.{ ProducerClosedException}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.util.Random

/**
  * Created by liuwens on 2017/9/18.
  */
object KafkaProducter {

  private val users = Array[String](
    "4A4D769EB9679C054DE81B973ED5D768", "8dfeb5aaafc027d89349ac9a20b3930f",
    "011BBF43B89BFBF266C865DF0397AA71", "f2a8474bf7bd94f0aabbd4cdd2c06dcf",
    "068b746ed4620d25e26055a9f804385f", "97edfc08311c70143401745a03a50706",
    "d7f141563005d1b5d0d3dd30138f3f62", "c8ee90aade1671a21336c721512b817a",
    "6b67c8c700427dee7552f81f3228c927", "a95f22eabc4fd4b580c011a3161a9d9d");

  private val random = new Random()

  private var pointer = -1

  def main(args:Array[String]): Unit =
  {
    val BROKER_LIST = "vm-linux-163.hkhf.hkgp.net:6667,vm-linux-164.hkhf.hkgp.net:6667,vm-linux-165.hkhf.hkgp.net:6667"
    val TOPIC = "spark_streaming_kafka_test"


    val param = new Properties();
    param.put("bootstrap.servers", BROKER_LIST)
    //props.put("serializer.class", "kafka.serializer.StringEncoder")
    param.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    param.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    param.setProperty("producer.type", "sync")
    param.setProperty("queue.enqueue.timeout.ms", "20000")

    val kafkaProducter = new KafkaProducer[String, String](param)


    while(true)
    {
      // prepare event data
      val event = new JSONObject()
      event.put("uid", getUserID)
      event.put("event_time", System.currentTimeMillis.toString)
      event.put("os_type", "Android")
      event.put("click_count", click)

      try
      {
        // produce event message
        val future = kafkaProducter.send(new ProducerRecord[String, String](TOPIC, getUserID, event.toJSONString))

        println(future.get().timestamp())
      }
      catch
      {
        case ex:ProducerClosedException => ex.printStackTrace();
      }

      println("Message sent: " + event)

      Thread.sleep(10000)
    }
  }

  def getUserID() : String =
  {
    pointer = pointer + 1
    if(pointer >= users.length)
    {
      pointer = 0
      users(pointer)
    }
    else
    {
      users(pointer)
    }
  }


  def click() : Double =
  {
    random.nextInt(10)
  }


}

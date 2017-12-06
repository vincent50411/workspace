package kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

/**
  * Created by liuwens on 2017/9/19.
  */
object KafkaProducter_002 {

  val BROKER_LIST = "vm-linux-163.hkhf.hkgp.net:6667"
  val TOPIC = "spark_streaming_kafka_test"

  def main(args: Array[String]): Unit = {
    val producer = createProducer()
    sendMsg(producer, TOPIC)
    producer.close()
  }

  def createProducer(): KafkaProducer[String, String] = {
    val param = new Properties()
    param.setProperty("bootstrap.servers", BROKER_LIST)
    param.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    param.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    param.setProperty("producer.type", "sync")
    //    param.setProperty("batch.num.message", "1")
    //    param.setProperty("queue.buffer.max.ms", "1000")
    param.setProperty("queue.enqueue.timeout.ms", "20000")

    new KafkaProducer[String, String](param)
  }

  def sendMsg(producer: KafkaProducer[String, String], topic: String): Unit = {

    for(i <- 1 to 5){
      //      Thread.sleep(i * 1000)
      val future = producer.send(new ProducerRecord[String, String](topic, String.valueOf(i), "xxxxxxx - " + i))
      println(future.get().timestamp())
    }
  }


}

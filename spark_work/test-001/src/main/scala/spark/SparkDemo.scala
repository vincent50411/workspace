package spark

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by liuwens on 2017/9/21.
  */
object SparkDemo {

  def main(args:Array[String]) :Unit={

    val sparkConfig = new SparkConf().setMaster("local[2]").setAppName("sfsdfsdf")

    val sparkContext = new SparkContext(sparkConfig)

    var result = sparkContext.parallelize((0 to 100)).map(item => item).reduce((x:Int, y:Int) => x + y)

    println("--> spark result value:" + result)

  }



}

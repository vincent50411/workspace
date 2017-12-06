import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by liuwens on 2017/9/14.
  */


object Helloworld {
  def main(args: Array[String]) {
    val logFile = "./README.md"  // Should be some file on your server.
    //val conf = new SparkConf().setAppName("Simple Application").setMaster("local")

    val conf = new SparkConf().setAppName("SogouResult").setMaster("local");


    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("h")).count()
    val numBs = logData.filter(line => line.contains("j")).count()
    println("Lines with h: %s, Lines with j: %s".format(numAs, numBs))
  }

}

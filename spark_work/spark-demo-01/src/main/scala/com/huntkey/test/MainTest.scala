package com.huntkey.test

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.log4j.Logger
/**
  * Created by liuwens on 2017/10/31.
  */
object MainTest {

  val logger = Logger.getLogger(MainTest.getClass.getName);

  def main(arg:Array[String]):Unit = {

    logger.info("----------------------------------- MainTest --------------------------------------------")

    val sc = new SparkConf().setAppName("test-app").setJars(List("D:\\spark_work\\spark-demo-01\\target\\spark-demo-01.jar"))
      .setMaster("spark://vm-linux-164.hkhf.hkgp.net:7077");//.setMaster("local")

    val sparkContext = new SparkContext(sc);

    //hdfs://vm-linux-161.hkhf.hkgp.net:8020
    val rdd = sparkContext.textFile("/user/root/test.text")

    val countValue = rdd.count();

    val  path = System.getProperty("java.class.path");

    println("--> path:" + path);

    println("Hello world! line item count:" + countValue);

    logger.info("----> :Hello world! line item count:" + countValue);

    logger.info("-------------------------------------------------------------------------------")

  }
}

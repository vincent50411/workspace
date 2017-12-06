name := "test-001"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.0.2" % "provided"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.2.0" % "provided"

libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % "2.2.0" % "provided"

libraryDependencies += "com.alibaba" % "fastjson" % "1.2.6"

// https://mvnrepository.com/artifact/redis.clients/jedis
libraryDependencies += "redis.clients" % "jedis" % "2.9.0"

// https://mvnrepository.com/artifact/org.scalaj/scalaj-http_2.11
libraryDependencies += "org.scalaj" % "scalaj-http_2.11" % "2.3.0"

// https://mvnrepository.com/artifact/com.alibaba/fastjson
libraryDependencies += "com.alibaba" % "fastjson" % "1.2.6"




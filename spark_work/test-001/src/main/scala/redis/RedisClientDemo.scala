package redis

import com.alibaba.fastjson.{JSONArray, JSONObject, JSON}
import redis.clients.jedis.{Jedis, JedisPool}

import scala.collection.JavaConverters
import scalaj.http.{Http, HttpOptions}

/**
  * Created by liuwens on 2017/9/20.
  */
object RedisClientDemo
{

  def main(args:Array[String]): Unit =
  {
    InternalRedisClient.makePool("localhost", 6379)

    val jedis = InternalRedisClient.getJedisPool.getResource;

    jedis.hset("user_002", "user_name", "张三_002")
    jedis.hset("user_002", "user_age", "17")


    println(jedis.hget("user_001", "user_name") + "   |  " + jedis.hget("user_001", "user_age"))

    var userMap = jedis.hgetAll("user_001")


    val keyList = JavaConverters.asScalaSetConverter(jedis.keys("user*")).asScala

    val mapList = keyList.map(keyItem => JavaConverters.mapAsScalaMapConverter(jedis.hgetAll(keyItem)).asScala).map(map => {
      map.get("user_age") match {
        case Some(x) => x.toInt;
        case None => 0
      }
    }).reduce(_ + _)


    val mapList2 = keyList.map(keyItem => JavaConverters.mapAsScalaMapConverter(jedis.hgetAll(keyItem)).asScala
      .groupBy(item => item._1).filter(item => item._1.equals("user_age")).mapValues(valueItem => valueItem.map(value => value._2)).flatMap(item => item._2)
    )

    println(mapList2)

    val userMapForScala = JavaConverters.mapAsScalaMapConverter(userMap).asScala;

    var g = userMapForScala.map(value => value._2)

    println(g)

    //httpService;

  }

  def saveMapService(): Unit =
  {



  }












  def httpService =
  {
    val result = Http("http://localhost:8080/testRequest").postData("""{"id":"12","json":"send json message to java"}""")
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000)).asString

    val jsonBody = result.body;

    println(jsonBody)

    val text = "[{\"name\":\"1111\"}]"
    val json = JSON.parseArray(text)

    val obj = json.getJSONObject(0)

    println(obj.get("name"))

    //val jsonList = JSON.parseArray(jsonBody)


    //println(jsonList)
  }



}

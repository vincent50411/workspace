package redis

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{JedisPool, Protocol}

/**
  * Created by liuwens on 2017/9/20.
  */
object InternalRedisClient extends Serializable
{

  var jedisPool:JedisPool = _;

  def makePool(redisHost: String, redisPort: Int):Unit =
  {
    makePool(redisHost, redisPort, Protocol.DEFAULT_TIMEOUT, GenericObjectPoolConfig.DEFAULT_MAX_TOTAL,
      GenericObjectPoolConfig.DEFAULT_MAX_IDLE, GenericObjectPoolConfig.DEFAULT_MIN_IDLE, true, false, 10000)
  }

  def makePool(redisHost: String, redisPort: Int, redisTimeout: Int, maxTotal: Int, maxIdle: Int, minIdle: Int): Unit =
  {
    makePool(redisHost, redisPort, redisTimeout, maxTotal, maxIdle, minIdle,true, false, 10000)
  }

  def makePool(redisHost: String, redisPort: Int, redisTimeout: Int, maxTotal: Int, maxIdle: Int, minIdle: Int, testOnBorrow: Boolean, testOnReturn: Boolean, maxWaitMillis: Long): Unit =
  {
    if(jedisPool == null)
    {
      val poolConfig = new GenericObjectPoolConfig()

      //最大连接数
      poolConfig.setMaxTotal(maxTotal)
      //最大空闲连接数
      poolConfig.setMaxIdle(maxIdle)
      // 设置最小空闲连接数或者说初始化连接数
      poolConfig.setMinIdle(minIdle)
      //借出去的连接是否测试可用性
      poolConfig.setTestOnBorrow(testOnBorrow)
      //返回的连接是否测试可用性
      poolConfig.setTestOnReturn(testOnReturn)
      //最大等待时间
      poolConfig.setMaxWaitMillis(maxWaitMillis)

      jedisPool = new JedisPool(poolConfig, redisHost, redisPort, redisTimeout)

      //声明一个钩子线程，执行销毁pool操作
      val hook = new Thread
      {
        override def run = jedisPool.destroy()
      }

      //注册一个钩子线程，当VM关闭时，运行
      sys.addShutdownHook(hook.run)
    }
}

  /**
    * 返回一个jedis pool实例
    * @return
    */
  def getJedisPool: JedisPool =
  {
    jedisPool
  }



}

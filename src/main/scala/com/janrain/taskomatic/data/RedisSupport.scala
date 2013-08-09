package com.janrain.taskomatic.data

import com.redis._

trait RedisSupport {
	private val redisHost: String = sys.env.getOrElse("REDIS_HOST", "localhost")
	private val redisPort: Int = sys.env.getOrElse("REDIS_PORT", 6379).toString.toInt

	lazy val client = new RedisClient(redisHost, redisPort)
}
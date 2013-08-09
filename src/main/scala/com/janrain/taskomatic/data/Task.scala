package com.janrain.taskomatic.data

import org.scalatra._
import com.redis._
import com.janrain.taskomatic.data._
import com.janrain.taskomatic.data.JsonSupport

case class Task(id: Long, description: String)

object Task {
	private val redisHost: String = sys.env.getOrElse("REDIS_HOST", "localhost")
	private val redisPort: Int = sys.env.getOrElse("REDIS_PORT", 6379).toString.toInt

	lazy val client = new RedisClient(redisHost, redisPort)
	val tasksKey: String = "tasks"

	def fetchAllTasks: Iterable[Task] = {
		val allTasks = client.hgetall[String, String](tasksKey).getOrElse(Map.empty)
		for (taskKey <- allTasks.keys) yield {
			Task(taskKey.toLong, allTasks(taskKey))
		}
	}

	def fetchTaskById(taskId: Long): Option[Task] = {
		client.hget(tasksKey, taskId).map(description => Task(taskId, description))
	}

	def createNewTask(task: Task) = {
		client.hset(tasksKey, task.id, task.description)
	}

	def modifyTaskById(taskId: Long, newDescription: String) = {
		client.hset(tasksKey, taskId, newDescription)
	}

	def deleteTaskById(taskId: Long) = {
		client.hdel(tasksKey, taskId)
	}

	def getTasksCount: Option[Long] = {
		client.hlen(tasksKey)
	}
}
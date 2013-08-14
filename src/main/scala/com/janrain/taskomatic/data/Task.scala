package com.janrain.taskomatic.data

import org.scalatra._
import com.redis._
import com.janrain.taskomatic.data.{ RedisSupport }

case class Task(id: Int, description: String)

object Task extends RedisSupport {

	def fetchAllTasks = {
		val allTasks = client.hgetall(tasksKey).getOrElse(Map.empty)
		for (taskKey <- allTasks.keys) yield {
			Task(taskKey.toInt, allTasks(taskKey))
		}
	}

	def fetchTaskById(taskId: Int): Option[Task] = {
		client.hget(tasksKey, taskId).map(description => Task(taskId, description))
	}

	def createNewTask(task: Task) = {
		client.hset(tasksKey, task.id, task.description)
	}

	def createNewTaskByDescription(description: String) {
		val taskId: java.util.UUID = java.util.UUID.randomUUID()
		client.hset(tasksKey, taskId, description)
	}

	def modifyTaskById(taskId: Int, newDescription: String) = {
		client.hset(tasksKey, taskId, newDescription)
	}

	def deleteTaskById(taskId: Int) = {
		client.hdel(tasksKey, taskId)
	}

	def deleteAll = {
		val allTasks = client.hgetall(tasksKey).getOrElse(Map.empty)
		for (k <- allTasks.keys) {
			client.hdel(tasksKey, k)
		}
	}

	def getTasksCount: Option[Long] = {
		client.hlen(tasksKey)
	}

}
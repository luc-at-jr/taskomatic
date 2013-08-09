package com.janrain.taskomatic

import org.scalatra._
import org.scalatra.json._
import org.json4s._
import scala.util.Random
import scala.concurrent._
import scala.concurrent.{ future, promise }
import ExecutionContext.Implicits.global

import com.janrain.taskomatic.data.{Task, JsonSupport}

class TaskomaticServlet extends TaskomaticStack with JsonSupport with MethodOverride {

  get("/") {
    jsonMethod
    Task.fetchAllTasks
  }

  get("/:id") {
    jsonMethod
  	val taskId: Long = params("id").toLong
    Task.fetchTaskById(taskId)
  }

  post("/") {
    jsonMethod
    val taskId: Long = new Random().nextInt.abs.toLong
    val description: String = params("description")
    val newTask = new Task(taskId, description)
    Task.createNewTask(newTask)
  }

  post("/json") {
    jsonMethod
  	val newTask = parsedBody.extract[Task]
    Task.createNewTask(newTask)
  }

  put("/:id") {
    jsonMethod
  	val taskId: Long = params("id").toLong
    val newTaskDescription = params("description")
    Task.modifyTaskById(taskId, newTaskDescription)
  }

  delete("/:id") {
    jsonMethod
    val taskId: Long = params("id").toLong
    Task.deleteTaskById(taskId)
  }

  get("/count") {
    jsonMethod
    Map("count" -> Task.getTasksCount)
  }
  
}

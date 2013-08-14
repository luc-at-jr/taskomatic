package com.janrain.taskomatic

import org.scalatra._
import org.scalatra.json._
import org.json4s._
import scala.concurrent._
import scala.concurrent.{ future, promise }
import ExecutionContext.Implicits.global

import com.janrain.taskomatic.data.JsonSupport
import com.janrain.taskomatic.data.Task

class TaskomaticServlet extends TaskomaticStack with JsonSupport with MethodOverride {

  get("/") {
    jsonMethod
    Task.fetchAllTasks
  }

  get("/:id") {
    jsonMethod
  	val taskId: Int = params("id").toInt
    Task.fetchTaskById(taskId)
  }

  post("/") {
    jsonMethod
    request.getHeader("Content-Type") match {
      case "application/json" => {
        val newTask = parsedBody.extract[Task]
        Task.createNewTask(newTask)
      }
      case "application/x-www-form-urlencoded" => {
        println(request.getHeader("Content-Type"))
        val description: String = params("description")
          Task.createNewTaskByDescription(description)
      }
      case _ => "Unknown content type"
    }

    /*if (request.getHeader("Content-Type").contains("application/json")) {
      val newTask = parsedBody.extract[Task]
      Task.createNewTask(newTask)
    } else {
      val description: String = params("description")
      Task.createNewTaskByDescription(description)
    }*/
  }

  post("/no-json") {
    val description: String = params("description")
    Task.createNewTaskByDescription(description)
  }

  put("/:id") {
  	val taskId: Int = params("id").toInt
    val newTaskDescription = params("description")
    Task.modifyTaskById(taskId, newTaskDescription)
  }

  delete("/:id") {
    val taskId: Int = params("id").toInt
    Task.deleteTaskById(taskId)
  }

  delete("/all") {
    Task.deleteAll
  }

  get("/count") {
    jsonMethod
    Map("tasks count" -> Task.getTasksCount)
  }
}

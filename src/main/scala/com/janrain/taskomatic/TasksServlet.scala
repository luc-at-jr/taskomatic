package com.janrain.taskomatic

import akka.actor.ActorSystem
import scala.concurrent.{ ExecutionContext, Future }
import org.scalatra._
import ExecutionContext.Implicits.global
import com.janrain.taskomatic.data._

class TasksServlet(system: ActorSystem) extends TaskomaticStack
  with MethodOverride
{

  get("/db/create-tasks-table") {
    TaskDAO.createTable
  }

  get("/db/destroy-tasks-table") {
    TaskDAO.destroyTable
  }

  get("/") {
    TaskDAO.listAllTasks
  }

  post("/") {
    TaskDAO.createTask(params("description").toString)
  }

  delete("/:id") {
    TaskDAO.deleteTaskById(params("id").toInt)
  }

  get("/:id") {
    TaskDAO.findTaskById(params("id").toInt)
  }

  put("/finish/:id") {
    TaskDAO.setTaskToDone(params("id").toInt)
  }

  get("/sort") {
    TaskDAO.sortByDescription
  }

}

package com.janrain.taskomatic

import akka.actor.ActorSystem
import akka.dispatch._
import scala.concurrent.{ ExecutionContext, Future }
import org.scalatra._
import org.scalatra.json._
import org.json4s._
import com.janrain.taskomatic.data.{ UserDAO }

class UsersServlet(system: ActorSystem) extends TaskomaticStack
	with MethodOverride
{

	get("/create-users-table") {
		UserDAO.createTables
	}

	get("/drop-users-table") {
		UserDAO.destroyTables
	}

	get("/") {
		UserDAO.listAllUsers
	}

	get("/user/:userId/tasks/:taskId") {
		UserDAO.getUserTasks(params("taskId").toInt, params("userId").toInt)
	}

	post("/") {
		UserDAO.createUser(params("username"), params("password"))
	}

	delete("/:id") {
		UserDAO.removeUserById(params("id").toInt)
	}

}
package com.janrain.taskomatic.data

import org.scalatra._
import spray.json._
import DefaultJsonProtocol._
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

import com.janrain.taskomatic.data.{ PostgresSupport, TaskDAO }

case class User(id: Int, username: String, password: String)

object UserJsonFormat extends DefaultJsonProtocol {
	implicit val userFormat = jsonFormat3(User)
}

object UserDAO extends PostgresSupport {
	
	import UserJsonFormat._

	object Users extends Table[User]("users") {
		def id       = column[Int]("id", O.PrimaryKey, O.AutoInc)
		def username = column[String]("username", O.NotNull)
		def password = column[String]("password", O.NotNull)

		def * = (id ~ username ~ password) <> (User, User.unapply _)
		
		def autoInc = username ~ password returning id

	}

	val usersDDL = Users.ddl

	def createTables() = session.withTransaction {
		usersDDL.create
		"You have successfully created the users table"
	}

	def destroyTables() = session.withTransaction {
		usersDDL.drop
	}

	def listAllUsers() = session.withTransaction {
		Query(Users).list.toJson
	}

	def createUser(username: String, password: String) = session.withSession {
		Users.autoInc.insert(username, password)
	}

	def removeUserById(id: Int) = session.withTransaction {
		Users.where(_.id === id).delete
	}

	def getUserTasks(taskId: Int, userId: Int) = session.withTransaction {
		val q = for {
			t <- TaskDAO.Tasks if t.id === taskId
			u <- Users if u.id === userId
		} yield "User: " + u.username.toString + ", task: " + t.description.toString
		q.list.toJson
	}

}
package com.janrain.taskomatic.data

import org.scalatra._
import spray.json._
import DefaultJsonProtocol._
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession
import com.github.tototoshi.slick.JodaSupport._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

import com.janrain.taskomatic.data.PostgresSupport

case class Task(id: Option[Int], description: String, timeCreated: DateTime, done: Boolean = false)

object TaskJsonProtocol extends DefaultJsonProtocol {
	val isoParser = ISODateTimeFormat.dateTimeParser()

	implicit object TaskJsonFormat extends JsonFormat[Task] {
		def read(json: JsValue): Task = json match {
			case JsArray(JsNumber(id) :: JsString(description) :: JsString(timeCreated) :: JsBoolean(done) :: Nil) =>
				new Task(id.toInt, description, isoParser.parseDateTime(timeCreated), done)
			case _ => deserializationError("Invalid Task")
		}
		def write(t: Task): JsValue = JsObject(
			"id" -> JsNumber(t.id.getOrElse(0)),
			"description" -> JsString(t.description),
			"timeCreated" -> JsString(t.timeCreated.toString()),
			"done" -> JsBoolean(t.done)
		)
	}
}

object TaskDAO extends PostgresSupport {

	import TaskJsonProtocol._

	object Tasks extends Table[Task]("tasks") {
		def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
		def description = column[String]("description", O.NotNull)
		def timeCreated = column[DateTime]("time_created", O.NotNull)
		def done = column[Boolean]("done", O.NotNull)
		
		def * = (id ~ description ~ timeCreated ~ done) <> (Task, Task.unapply _)

		def autoInc = description ~ timeCreated ~ done returning id
	}

	val tasksDDL = Tasks.ddl

	val allTasks = Query(Tasks)
	
	def createTable() = session.withTransaction {
		tasksDDL.create
	}

	def destroyTable() = session.withTransaction {
		tasksDDL.drop
	}

	def listAllTasks() = session.withTransaction {
		allTasks.list.toJson
	}

	def createTask(description: String): Unit = session.withTransaction {
		val now: DateTime = new DateTime()
		val done: Boolean = false
		Tasks.autoInc.insert(description, now, done)
	}

	def deleteTaskById(id: Int): Unit = session.withTransaction {
		Tasks.filter(_.id === id).delete
	}

	def findTaskById(id: Int): Unit = session.withTransaction {
		allTasks.filter(_.id === id).map(t => t).list.toJson
	}

	def setTaskToDone(id: Int): Unit = session.withTransaction {
		val q = for { t <- Tasks if t.id is id }
			yield t.done
		q.update(true)

	}

	def sortByDescription() = session.withTransaction {
		allTasks.sortBy(_.description).list.toJson
	}

}
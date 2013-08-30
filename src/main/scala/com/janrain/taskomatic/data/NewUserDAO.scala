/*trait UserPostgresDriver extends PostgresDriver
	with PgArraySupport
	with PostGISSupport
{

}

object UserPostgresDriver extends UserPostgresDriver

import com.janrain.taskomatic.data.Task

case class User(username: String, password: String, tasks: List[Task])

object Users extends Table[User]("users") {
	def id       = column[Int]("id", O.AutoInc, O.PrimaryKey)
	def username = column[String]("username", O.NotNull)
	def password = column[String]("password", O.NotNull)
	def tasks    = column[List[Task]]("tasks", O.DBType("array"))

	def * = id ~ username ~ password ~ tasks <> (User, User.unapply _)

	def byId(ids: Int*) = Users.where(_.id inSetBind ids).map(u => u)

	def byUsername(usernames: String*) = Users.where(_.usernames @& usernames.toList.bind).map(u => u)

}*/
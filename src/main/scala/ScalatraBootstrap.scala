import com.janrain.taskomatic._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  // Establishing our baseline route for the "tasks" resource as /api/v1/tasks
  val rootUrl = "/api"
  val apiV1Url = "/v1"
  val taskServletUrl = "/tasks/*"
  val tasksServletUrlEndpoint: String = rootUrl + apiV1Url + taskServletUrl

  // Establishing out baseline route for the "users" resource as /api/v1/users

  val userServletUrl = "/users/*"
  val usersServletUrlEndpoint: String = rootUrl + apiV1Url + userServletUrl

  // What happens upon initialization
  override def init(context: ServletContext) {
    context.mount(new TaskomaticServlet, tasksServletUrlEndpoint)
  }
}

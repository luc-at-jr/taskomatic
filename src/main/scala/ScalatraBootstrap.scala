import com.janrain.taskomatic._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  val rootUrl = "/api"
  val apiV1Url = "/v1"
  val taskServletUrl = "/tasks/*"
  val taskomaticServletUrlEndpoint: String = rootUrl + apiV1Url + taskServletUrl

  override def init(context: ServletContext) {
    context.mount(new TaskomaticServlet, taskomaticServletUrlEndpoint)
    //context.initParameters("org.scalatra.environment")
  }
}


import configs.SpringConfiguration
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.ApplicationContext
import play.api.{Application, GlobalSettings}

/**
 * Global
 * Created by debop on 2014. 1. 22..
 */
object Global extends GlobalSettings {

    lazy val log = LoggerFactory.getLogger(getClass)

    var ctx: ApplicationContext = _

    override def onStart(app: Application) {
        log.info("Application을 시작합니다...")

        super.onStart(app)
        ctx = new AnnotationConfigApplicationContext(classOf[SpringConfiguration])

        log.info("Application을 시작했습니다.")
    }

    override def getControllerInstance[A](controllerClass: Class[A]): A = {
        ctx.getBean[A](controllerClass)
    }
}

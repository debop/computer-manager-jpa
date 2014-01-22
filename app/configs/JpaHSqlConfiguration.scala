package configs

import org.springframework.context.annotation.Configuration
import kr.debop4s.data.jpa.config.AbstractHSqlJpaConfiguration
import java.util.Properties
import org.hibernate.cfg.AvailableSettings

/**
 * JpaHSqlConfiguration
 * Created by debop on 2014. 1. 22..
 */
@Configuration
class JpaHSqlConfiguration extends AbstractHSqlJpaConfiguration {

    def getMappedPackageNames: Array[String] =
        Array(classOf[Company].getPackage.getName)

    override def jpaProperties(): Properties = {
        val props = super.jpaProperties()
        props.put(AvailableSettings.HBM2DDL_AUTO, "create-drop")

        props
    }
}

package configs

import java.util.Properties
import kr.debop4s.data.jpa.config.AbstractHSqlJpaConfiguration
import models.Company
import org.hibernate.cfg.AvailableSettings
import org.springframework.context.annotation.Configuration

/**
 * JpaHSqlConfiguration
 * Created by debop on 2014. 1. 22..
 */
@Configuration
class HSqlJpaConfiguration extends AbstractHSqlJpaConfiguration {

    def getMappedPackageNames: Array[String] =
        Array(classOf[Company].getPackage.getName)

    override def jpaProperties(): Properties = {
        val props = super.jpaProperties()
        props.put(AvailableSettings.HBM2DDL_AUTO, "create-drop")

        props
    }
}

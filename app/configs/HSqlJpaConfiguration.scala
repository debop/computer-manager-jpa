package configs

import java.util.Properties
import kr.hconnect.data.jpa.spring.HSqlConfigBase
import models.Company
import org.hibernate.cfg.AvailableSettings
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * JpaHSqlConfiguration
 * Created by debop on 2014. 1. 22..
 */
@Configuration
@EnableTransactionManagement
class HSqlJpaConfiguration extends HSqlConfigBase {

    def getMappedPackageNames: Array[String] =
        Array(classOf[Company].getPackage.getName)

    override def jpaProperties(): Properties = {
        val props = super.jpaProperties()
        props.put(AvailableSettings.HBM2DDL_AUTO, "create-drop")

        props
    }
}

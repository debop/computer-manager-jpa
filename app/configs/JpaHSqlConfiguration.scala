package configs

import domains.models.Company
import java.util.Properties
import kr.debop4s.data.hibernate.config.AbstractHSqlHibernateConfiguration
import org.hibernate.cfg.AvailableSettings
import org.springframework.context.annotation.Configuration

/**
 * JpaHSqlConfiguration
 * Created by debop on 2014. 1. 22..
 */
@Configuration
class JpaHSqlConfiguration extends AbstractHSqlHibernateConfiguration {

    def getMappedPackageNames: Array[String] =
        Array(classOf[Company].getPackage.getName)

    override def hibernateProperties(): Properties = {
        val props = super.hibernateProperties()
        props.put(AvailableSettings.HBM2DDL_AUTO, "create-drop")

        props
    }
}

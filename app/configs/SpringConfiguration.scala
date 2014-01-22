package configs

import org.springframework.context.annotation.{Import, ComponentScan, Configuration}
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * SpringConfiguration
 * Created by debop on 2014. 1. 22..
 */
@Configuration
@EnableTransactionManagement
// @EnableJpaRepositories(basePackages = Array("repository"))
@ComponentScan(basePackages = Array("controllers", "services", "repository"))
@Import(Array(classOf[JpaHSqlConfiguration]))
class SpringConfiguration {

}

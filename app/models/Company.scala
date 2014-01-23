package models

import javax.persistence._
import kr.debop4s.core.spring.Springs
import kr.debop4s.data.model.HibernateEntity
import org.hibernate.annotations.{DynamicUpdate, DynamicInsert}
import repository.CompanyJpaRepository
import scala.collection.JavaConversions._

/**
 * Company
 * Created by debop on 2014. 1. 22..
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Access(AccessType.FIELD)
class Company extends HibernateEntity[java.lang.Long] {

    @Id
    @GeneratedValue
    @Column(name = "company_id")
    private var id: java.lang.Long = _

    def getId: java.lang.Long = id

    protected def setId(x: java.lang.Long) {
        id = x
    }

    // @Constraints.Required
    var name: String = _
}

object Company {

    lazy val repository: CompanyJpaRepository = Springs.getBean[CompanyJpaRepository](classOf[CompanyJpaRepository])

    def apply(id: Long, name: String) = {
        val company = new Company
        company.setId(id)
        company.name = name
        company
    }

    def unapply(company: Company) = {
        Some(company.id, company.name)
    }

    def findById(id: java.lang.Long) = repository.findOne(id)

    def options = {
        val companies = repository.findAll()
        companies.map(company => (company.getId.toString, company.name)).toSeq
    }
}

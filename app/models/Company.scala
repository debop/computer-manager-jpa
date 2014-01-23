package models

import javax.persistence._
import kr.hconnect.core.spring.Springs
import kr.hconnect.data.model.EntityBase
import org.hibernate.annotations.{DynamicUpdate, DynamicInsert}
import repository.CompanyJpaRepository
import scala.collection.JavaConversions._
import com.google.common.base.Objects.ToStringHelper
import kr.hconnect.core.tools.HashTool

/**
 * Company
 * Created by debop on 2014. 1. 22..
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Access(AccessType.FIELD)
class Company extends EntityBase[java.lang.Long] {

    @Id
    @GeneratedValue
    @Column(name = "companyId")
    private var id: java.lang.Long = _

    def getId: java.lang.Long = id

    protected def setId(x: java.lang.Long) {
        id = x
    }

    var name: String = _

    override def hashCode(): Int = HashTool.compute(name)

    override def buildStringHelper(): ToStringHelper =
        super.buildStringHelper()
            .add("name", name)
}

object Company {

    lazy val repository: CompanyJpaRepository = Springs.getBean[CompanyJpaRepository](classOf[CompanyJpaRepository])

    def apply(id: Long, name: String) = {
        val company = new Company()
        company.setId(id)
        company.name = name
        company
    }

    def unapply(company: Company) = {
        Some(company.id, company.name)
    }

    def findById(id: Long) = repository.findOne(id)

    def options = {
        val companies = repository.findAll()
        companies.map(company => (company.getId.toString, company.name)).toSeq
    }
}

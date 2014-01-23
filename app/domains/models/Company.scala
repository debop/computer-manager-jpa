package domains.models

import javax.persistence._
import kr.debop4s.data.model.HibernateEntity
import org.hibernate.annotations.{DynamicUpdate, DynamicInsert}

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

    def apply(id: Long, name: String) = {
        val company = new Company
        company.setId(id)
        company.name = name
        company
    }

    def unapply(company: Company) = {
        Some(company.id, company.name)
    }
}

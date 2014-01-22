package models

import javax.persistence._
import org.hibernate.annotations.{DynamicUpdate, DynamicInsert}
import kr.debop4s.data.model.HibernateEntity

/**
 * Company
 * Created by debop on 2014. 1. 22..
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Access(AccessType.FIELD)
class Company extends HibernateEntity[Long] {

    @Id
    @GeneratedValue
    private var id: Long = _

    def getId: Long = id

    protected def setId(x: Long) {
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

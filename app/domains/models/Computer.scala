package models

import org.hibernate.annotations.{DynamicInsert, DynamicUpdate}
import javax.persistence._
import kr.debop4s.data.model.HibernateEntity
import java.util.Date
import kr.debop4s.core.utils.Hashs
import repository.CompanyRepository

/**
 * Computer
 * Created by debop on 2014. 1. 22..
 */
@Entity
@SequenceGenerator(name = "computer_seq", sequenceName = "computer_seq")
@DynamicInsert
@DynamicUpdate
@Access(AccessType.FIELD)
class Computer extends HibernateEntity[Long] {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "computer_seq")
    var id: Long = _

    def getId: Long = id

    var name: String = _

    // @Formats.DateTime(pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    var introduced: Date = _

    @Temporal(TemporalType.DATE)
    var discontinued: Date = _

    @ManyToOne(cascade = Array(CascadeType.MERGE))
    var company: Company = _

    override def hashCode(): Int = Hashs.compute(name)
}

object Computer {

    // TODO: 여기서 Repository를 Injection 할 수 있어야 합니다.
    lazy val companyRepository: CompanyRepository = new CompanyRepository()

    def apply(id: Long, name: String, introduced: Option[Date], discontinued: Option[Date], companyId: Option[Long]) = {
        val computer = new Computer
        computer.id = id
        computer.name = name
        computer.introduced = introduced.getOrElse(null)
        computer.discontinued = discontinued.getOrElse(null)
        if (companyId != None)
            computer.company = companyRepository.findById(companyId.get).getOrElse(null)

        computer
    }

    def unapply(computer: Computer) = {
        Some(computer.id,
            computer.name,
            Some(computer.introduced),
            Some(computer.discontinued),
            if (computer.company != null) Some(computer.company.getId) else None)
    }
}

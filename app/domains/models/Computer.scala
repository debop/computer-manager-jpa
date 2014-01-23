package domains.models

import domains.repository.CompanyRepository
import java.util.Date
import javax.persistence._
import kr.debop4s.core.spring.Springs
import kr.debop4s.core.utils.Hashs
import kr.debop4s.data.model.HibernateEntity
import org.hibernate.annotations.{DynamicInsert, DynamicUpdate}

/**
 * Computer
 * Created by debop on 2014. 1. 22..
 */
@Entity
@SequenceGenerator(name = "computer_seq", sequenceName = "computer_seq")
@DynamicInsert
@DynamicUpdate
@Access(AccessType.FIELD)
class Computer extends HibernateEntity[java.lang.Long] {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "computer_seq")
    var id: java.lang.Long = _

    def getId: java.lang.Long = id

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

    lazy val companyRepository: CompanyRepository = Springs.getBean(classOf[CompanyRepository])

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
        Some(computer.id.toLong,
            computer.name,
            Some(computer.introduced),
            Some(computer.discontinued),
            if (computer.company != null) Some(computer.company.getId.toLong) else None)
    }
}

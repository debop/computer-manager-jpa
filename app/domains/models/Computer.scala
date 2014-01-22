package models

import org.hibernate.annotations.{DynamicInsert, DynamicUpdate}
import javax.persistence._
import kr.debop4s.data.model.HibernateEntity
import java.util.Date
import kr.debop4s.core.utils.Hashs

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

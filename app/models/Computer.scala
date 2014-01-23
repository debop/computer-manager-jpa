package models

import java.util.Date
import javax.persistence._
import kr.hconnect.core.spring.Springs
import kr.hconnect.core.tools.{StringTool, HashTool}
import kr.hconnect.data.model.EntityBase
import org.hibernate.annotations.{DynamicInsert, DynamicUpdate}
import org.springframework.data.domain.{Sort, PageRequest}
import repository.ComputerJpaRepository
import scala.collection.JavaConversions._
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import com.google.common.base.Objects.ToStringHelper

/**
 * Computer
 * Created by debop on 2014. 1. 22..
 */
@Entity
@SequenceGenerator(name = "computer_seq", sequenceName = "computer_seq")
@DynamicInsert
@DynamicUpdate
@Access(AccessType.FIELD)
class Computer extends EntityBase[java.lang.Long] {

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
    @JoinColumn(name = "companyId", nullable = true)
    var company: Company = _

    override def hashCode(): Int = HashTool.compute(name)

    override def buildStringHelper(): ToStringHelper =
        super.buildStringHelper()
            .add("name", name)
            .add("introduced", introduced)
            .add("discontinued", discontinued)
            .add("company", company)
}

object Computer {

    lazy val log = LoggerFactory.getLogger(getClass)
    lazy val repository: ComputerJpaRepository = Springs.getBean(classOf[ComputerJpaRepository])

    def apply(id: Option[Long], name: String, introduced: Option[Date], discontinued: Option[Date], companyId: Option[Long]) = {
        val computer = new Computer()
        computer.id = id.getOrElse(0L).asInstanceOf[java.lang.Long]
        computer.name = name
        computer.introduced = introduced.getOrElse(null)
        computer.discontinued = discontinued.getOrElse(null)

        if (companyId != None)
            computer.company = Company.findById(companyId.get)

        computer
    }

    def unapply(computer: Computer) = {
        Some(Some(computer.id.toLong),
            computer.name,
            if (computer.introduced != null) Some(computer.introduced) else None,
            if (computer.discontinued != null) Some(computer.discontinued) else None,
            if (computer.company != null) Some(computer.company.getId.toLong) else None)
    }

    @Transactional
    def update(computer: Computer) {
        try {
            repository.saveAndFlush(computer)
        } catch {
            case e: Exception => log.error("저장하는데 실패했습니다.", e)
        }
    }

    @Transactional
    def save(computer: Computer) {
        try {
            repository.saveAndFlush(computer)
        } catch {
            case e: Exception => log.error("저장하는데 실패했습니다.", e)
        }
    }

    @Transactional
    def delete(id: Long) {
        repository.delete(id)
        repository.flush()
    }

    def findById(id: Long) = repository.findOne(id)

    val propertyNames = List("null", "id", "name", "introduced", "discontinued", "company.name")

    def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = ""): Pager[(Computer, Option[Company])] = {

        log.debug(s"list page=[$page], orderBy=[$orderBy], filter=[$filter]")

        val sort = if (orderBy > 0) Sort.Direction.ASC else Sort.Direction.DESC
        val sortProperty = propertyNames(math.abs(orderBy))
        val pageable = new PageRequest(page, pageSize, sort, sortProperty)

        log.debug(s"pageable=$pageable")

        val computers =
            if (StringTool.isEmpty(filter)) repository.findAll(pageable)
            else repository.findByNameLike("%" + filter + "%", pageable)

        val map = computers.getContent.map(c => (c, Option(c.company))).toSeq
        Pager(map,
            pageable.getPageNumber,
            pageable.getPageNumber * pageable.getPageSize,
            computers.getTotalElements)
    }
}

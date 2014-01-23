package domains.repository

import domains.models.{Company, Computer, Pager}
import kr.debop4s.core.utils.Strings
import kr.debop4s.data.hibernate.repository.HibernateDao
import org.hibernate.criterion.{MatchMode, Restrictions, DetachedCriteria}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.{Page, Pageable}
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import scala.collection.JavaConversions._

/**
 * ComputerRepository
 * Created by debop on 2014. 1. 22..
 */
@Repository
@Transactional
class ComputerRepository {

    lazy val log = LoggerFactory.getLogger(getClass)

    @Autowired val dao: HibernateDao = null

    def update(computer: Computer) {
        dao.update(computer)
        dao.flush()
    }

    def save(computer: Computer) {
        dao.save(computer)
        dao.flush()
    }

    def delete(id: Long) {
        dao.deleteById(classOf[Computer], id)
        dao.flush()
    }

    def findById(id: Long): Some[Computer] = Some(dao.get(classOf[Computer], id))

    @Transactional
    def list(pageable: Pageable, filter: String = ""): Pager[(Computer, Option[Company])] = {
        var dc = DetachedCriteria.forClass(classOf[Computer])

        if (!Strings.isEmpty(filter))
            dc = dc.add(Restrictions.ilike("name", filter, MatchMode.ANYWHERE))

        val page = dao.getPage(dc, pageable)

        log.debug(s"loaded Computer=[$page]")

        val computers = page.asInstanceOf[Page[Computer]].getContent.map(c => (c, Option(c.company)))
        Pager(computers, page.getNumber, page.getNumber * page.getSize, page.getTotalElements)
    }
}

package repository

import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.hibernate.SessionFactory
import kr.debop4s.data.hibernate.repository.HibernateDao
import models.Computer
import org.springframework.data.domain.{Page, Pageable}
import org.hibernate.criterion.{MatchMode, Restrictions, DetachedCriteria}
import kr.debop4s.core.utils.Strings
import org.springframework.transaction.annotation.Transactional

/**
 * ComputerRepository
 * Created by debop on 2014. 1. 22..
 */
@Repository
@Transactional
class ComputerRepository {

    @Autowired val sessionFactory: SessionFactory = null
    lazy val dao: HibernateDao = new HibernateDao(sessionFactory)

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

    def page(pageable: Pageable, filter: String = ""): Page[Computer] = {
        val dc = DetachedCriteria.forClass(classOf[Computer])
        if (!Strings.isEmpty(filter))
            dc.add(Restrictions.ilike("name", filter, MatchMode.ANYWHERE))
        dao.getPage(dc, pageable).asInstanceOf[Page[Computer]]
    }
}

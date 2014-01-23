package domains.repository

import domains.models.Company
import kr.debop4s.data.hibernate.repository.HibernateDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * CompanyRepository
 * Created by debop on 2014. 1. 22..
 */
@Repository
@Transactional
class CompanyRepository {

    @Autowired val dao: HibernateDao = null

    def findById(id: Long): Option[Company] = Some(dao.get(classOf[Company], id))

    def options: List[(String, String)] = {
        val companies = dao.findAll(classOf[Company])
        companies.map(c => (c.getId.toString, c.name))
    }
}

package repository

import org.hibernate.SessionFactory
import kr.debop4s.data.hibernate.repository.HibernateDao
import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import models.Company

/**
 * CompanyRepository
 * Created by debop on 2014. 1. 22..
 */
@Repository
class CompanyRepository {

    @Autowired val sessionFactory: SessionFactory = null
    lazy val dao: HibernateDao = new HibernateDao(sessionFactory)

    def findById(id: Long): Company = dao.get(classOf[Company], id)

    def options: List[(String, String)] = {
        val companies = dao.findAll(classOf[Company])
        companies.map(c => (c.getId.toString, c.name))
    }
}

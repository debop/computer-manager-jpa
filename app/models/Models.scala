package models

import java.util.Date
import anorm._
import anorm.SqlParser._

import scala.language.postfixOps
import play.api.db._
import play.api.Play.current


case class Company(id: Pk[Long] = NotAssigned, name: String)

case class Computer(id: Pk[Long] = NotAssigned,
    name: String,
    introduced: Option[Date],
    discontinued: Option[Date],
    companyId: Option[Long])

case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
    lazy val prev = Option(page - 1).filter(_ >= 0)
    lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

object Computer {

    val simple = {
        get[Pk[Long]]("computer.id") ~
        get[String]("computer.name") ~
        get[Option[Date]]("computer.introduced") ~
        get[Option[Date]]("computer.discontinued") ~
        get[Option[Long]]("computer.company_id") map {
            case id ~ name ~ introduced ~ discontinued ~ companyId =>
                Computer(id, name, introduced, discontinued, companyId)
        }
    }

    val withCompany = Computer.simple ~ (Company.simple ?) map {
        case computer ~ company => (computer, company)
    }

    def findById(id: Long): Option[Computer] = {
        DB.withConnection {
            implicit conn =>
                SQL("select * from computer where id={id}").on('id -> id).as(Computer.simple.singleOpt)
        }
    }

    def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Page[(Computer, Option[Company])] = {
        val offset = pageSize * page

        DB.withConnection {
            implicit conn =>

                val computers = SQL(
                    """
                        select * from computer
                        left join company on computer.company_id = company.id
                        where computer.name like {filter}
                        order by {orderBy} nulls last
                        limit {pageSize} offset {offset}
                    """
                )
                    .on(
                        'pageSize -> pageSize,
                        'offset -> offset,
                        'filter -> filter,
                        'orderBy -> orderBy
                    )
                    .as(Computer.withCompany *)

                val totalRows = SQL(
                    """
                        select count(*) from computer
                        left join company on computer.company_id = company.id
                        where computer.name like {filter}
                    """
                )
                    .on('filter -> filter)
                    .as(scalar[Long].single)

                Page(computers, page, offset, totalRows)
        }
    }

    def update(id: Long, computer: Computer) = {
        DB.withConnection {
            implicit conn =>
                SQL(
                    """
                           update computer
                           set name={name}, introduced={introduced}, discontinued={discontinued}, company_id={company_id}
                           where id = {id}
                    """
                ).on(
                        'id -> id,
                        'name -> computer.name,
                        'introduced -> computer.introduced,
                        'discontinued -> computer.discontinued,
                        'company_id -> computer.companyId
                    ).executeUpdate()

        }
    }

    def insert(computer: Computer) = {
        DB.withConnection {
            implicit conn =>
                SQL(
                    """
                           insert into computer values(
                           (select next value for computer_seq),
                           {name},
                           {introduced},
                           {discontinued},
                           {company_id})
                    """
                ).on(
                        'name -> computer.name,
                        'introduced -> computer.introduced,
                        'discontinued -> computer.discontinued,
                        'company_id -> computer.companyId
                    ).executeUpdate()

        }
    }

    def delete(id: Long) = {
        DB.withConnection {
            implicit conn =>
                SQL("delete from computer where id={id}").on('id -> id).executeUpdate
        }
    }
}

object Company {

    val simple = {
        get[Pk[Long]]("company.id") ~
        get[String]("company.name") map {
            case id ~ name => Company(id, name)
        }
    }

    def options: Seq[(String, String)] = DB.withConnection {
        implicit connection =>
            SQL("select * from company order by name").as(Company.simple *).map(c => c.id.toString -> c.name)
    }
}
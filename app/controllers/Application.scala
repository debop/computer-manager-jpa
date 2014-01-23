package controllers

import models.{Company, Computer}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.{Sort, PageRequest}
import org.springframework.stereotype.Component
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import repository.{ComputerJpaRepository, CompanyJpaRepository}
import views._
import play.api.Logger
import org.springframework.transaction.annotation.Transactional
import org.slf4j.LoggerFactory

object Application extends Controller {

    lazy val log = LoggerFactory.getLogger(getClass)

    val Home = Redirect(routes.Application.list(0, 2, ""))

    val computerForm = Form(
        mapping(
            "id" -> optional(longNumber),
            "name" -> nonEmptyText,
            "introduced" -> optional(date("yyyy-MM-dd")),
            "discontinued" -> optional(date("yyyy-MM-dd")),
            "company" -> optional(longNumber)
        )(Computer.apply)(Computer.unapply)
    )


    def index = Action {
        Home
    }

    def list(page: Int, orderBy: Int, filter: String) = Action { implicit request =>
        val list = Computer.list(page, 10, orderBy, filter)
        Ok(html.list(list, orderBy, filter))
    }

    def edit(id: Long) = Action {
        Option(Computer.findById(id)).map {
            computer =>
                Ok(html.editForm(id, computerForm.fill(computer), companyOptions))
        }.getOrElse {
            NotFound
        }
    }

    def update(id: Long) = Action { implicit request =>
        computerForm.bindFromRequest().fold(
            formWithErrors => {
                Logger.warn(s"formWithErrors=$formWithErrors")
                BadRequest(html.editForm(id, formWithErrors, companyOptions))
            },
            computer => {
                computer.id = id
                log.debug(s"Update computer=[$computer]")
                Computer.update(computer)
                Home.flashing("success" -> s"Computer ${computer.name} has been updated")
            }
        )
    }

    def create = Action {
        Ok(html.createForm(computerForm, companyOptions))
    }

    def save = Action {
        implicit request =>
            computerForm.bindFromRequest.fold(
                formWithError => {
                    log.debug(s"Computer 정보에 오류가 있습니다. ${formWithError.errors}")
                    BadRequest(html.createForm(formWithError, companyOptions))
                },
                computer => {
                    log.debug(s"새로운 Computer를 저장합니다. computer=$computer")
                    Computer.save(computer)
                    Home.flashing("success" -> s"Computer ${computer.name} has been created")
                }
            )
    }

    def delete(id: Long) = Action {
        log.debug(s"Delete Computer. id=[$id]")
        Computer.delete(id)
        Home.flashing("success" -> "Computer has been deleted")
    }

    private def companyOptions: Seq[(String, String)] = {
        Company.options
    }

}
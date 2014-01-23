package controllers

import models.Computer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.{Sort, PageRequest}
import org.springframework.stereotype.Component
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import repository.{ComputerJpaRepository, CompanyJpaRepository}
import views._

@Component
class Application extends Controller {

    //    @Autowired val computerRepository: ComputerRepository = null
    //    @Autowired val companyRepository: CompanyRepository = null

    @Autowired val computerRepository: ComputerJpaRepository = null
    @Autowired val companyRepository: CompanyJpaRepository = null

    val Home = Redirect(routes.Application.list(0, 2, ""))

    val computerForm = Form(
        mapping(
            "id" -> longNumber,
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

        val pageable = new PageRequest(page, 10, Sort.Direction.ASC, "id")
        val list = Computer.list(pageable, filter)
        Ok(html.list(list, orderBy, filter))
    }

    def edit(id: Long) = Action {
        Option(computerRepository.findOne(id)).map {
            computer =>
                Ok(html.editForm(id, computerForm.fill(computer), companyOptions))
        }.getOrElse {
            NotFound
        }
    }

    def update(id: Long) = Action { implicit request =>
        computerForm.bindFromRequest().fold(
            formWithErrors => BadRequest(html.editForm(id, formWithErrors, companyOptions)),
            computer => {
                computerRepository.save(computer)
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
                formWithError => BadRequest(html.createForm(formWithError, companyOptions)),
                computer => {
                    computerRepository.save(computer)
                    Home.flashing("success" -> s"Computer ${computer.name} has been created")
                }
            )
    }

    def delete(id: Long) = Action {
        computerRepository.delete(id)
        Home.flashing("success" -> "Computer has been deleted")
    }

    private def companyOptions: Seq[(String, String)] = {
        List()
    }

}
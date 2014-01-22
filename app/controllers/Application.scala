package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import views._
import org.springframework.stereotype.Component
import repository.{CompanyRepository, ComputerRepository}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.{Sort, PageRequest}
import models.Computer
import kr.debop4s.core.utils.Mappers

@Component
class Application extends Controller {

    @Autowired val computerRepository: ComputerRepository = null
    @Autowired val companyRepository: CompanyRepository = null

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
        val list = computerRepository.page(pageable, filter)
        Ok(html.list(list, orderBy, filter))
    }

    def edit(id: Long) = Action {
        computerRepository.findById(id).map {
            computer =>
                val computerDto = Mappers.map[ComputerDto](computer)
                Ok(html.editForm(id, computerForm.fill(computerDto), companyRepository.options))
        }.getOrElse {
            NotFound
        }
    }

    def update(id: Long) = Action { implicit request =>
        computerForm.bindFromRequest().fold(
            formWithErrors => BadRequest(html.editForm(id, formWithErrors, companyRepository.options)),
            computerDto => {
                val computer = computerRepository.findById(id).get
                Mappers.map(computerDto, computer)
                computerRepository.update(computer)
                Home.flashing("success" -> s"Computer ${computer.name} has been updated")
            }
        )
    }

    def create = Action {
        Ok(html.createForm(computerForm, companyRepository.options))
    }

    def save = Action {
        implicit request =>
            computerForm.bindFromRequest.fold(
                formWithError => BadRequest(html.createForm(formWithError, companyRepository.options)),
                computerDto => {
                    val computer = Mappers.map[Computer](computerDto)
                    computerRepository.save(computer)
                    Home.flashing("success" -> s"Computer ${computer.name} has been created")
                }
            )
    }

    def delete(id: Long) = Action {
        computerRepository.delete(id)
        Home.flashing("success" -> "Computer has been deleted")
    }

}
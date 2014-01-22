package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import anorm._
import models._
import views._


object Application extends Controller {

    val Home = Redirect(routes.Application.list(0, 2, ""))

    val computerForm = Form(
        mapping(
            "id" -> ignored(NotAssigned: Pk[Long]),
            "name" -> nonEmptyText,
            "introduced" -> optional(date("yyyy-MM-dd")),
            "discontinued" -> optional(date("yyyy-MM-dd")),
            "company" -> optional(longNumber)
        )(Computer.apply)(Computer.unapply)

    )

    def index = Action {Home}

    def list(page: Int, orderBy: Int, filter: String) = Action {
        implicit request =>

            val list = models.Computer.list(page = page, orderBy = orderBy, filter = ("%" + filter + "%"))
            Ok(html.list(list, orderBy, filter))
    }

    def edit(id: Long) = Action {
        Computer.findById(id).map {
            computer =>
                Ok(html.editForm(id, computerForm.fill(computer), Company.options))
        } getOrElse {
            NotFound
        }
    }

    def update(id: Long) = Action {
        implicit request =>
            computerForm.bindFromRequest().fold(
                formWithErrors => BadRequest(html.editForm(id, formWithErrors, Company.options)),
                computer => {
                    Computer.update(id, computer)
                    Home.flashing("success" -> s"Computer ${computer.name} has been updated")
                }
            )
    }

    def create = Action {
        Ok(html.createForm(computerForm, Company.options))
    }

    def save = Action {
        implicit request =>
            computerForm.bindFromRequest.fold(
                formWithError => BadRequest(html.createForm(formWithError, Company.options)),
                computer => {
                    Computer.insert(computer)
                    Home.flashing("success" -> s"Computer ${computer.name} has been created")
                }
            )
    }

    def delete(id:Long) = Action {
        Computer.delete(id)
        Home.flashing("success" -> "Computer has been deleted")
    }

}
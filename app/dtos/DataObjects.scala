package dtos

import java.util.Date

/**
 * DataObjects
 * Created by debop on 2014. 1. 22..
 */
case class CompanyDto(id: Long, name: String)

case class ComputerDto(id: Long,
    name: String,
    introduced: Option[Date],
    discontinued: Option[Date],
    companyId: Option[Long])

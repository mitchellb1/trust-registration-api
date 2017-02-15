package uk.gov.hmrc.trustregistration.models.beneficiaries

import play.api.libs.json.Json
import uk.gov.hmrc.trustregistration.models.Individual

case class EmployeeBeneficiary(individual: Individual)

object EmployeeBeneficiary {
  implicit val employeeBeneficiaryFormats = Json.format[EmployeeBeneficiary]
}


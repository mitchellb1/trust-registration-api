package uk.gov.hmrc.trustregistration.models.beneficiaries

import play.api.libs.json.Json
import uk.gov.hmrc.trustregistration.models.Individual

case class DirectorBeneficiary(individual: Individual)

object DirectorBeneficiary {
  implicit val directorBeneficiaryFormats = Json.format[DirectorBeneficiary]
}

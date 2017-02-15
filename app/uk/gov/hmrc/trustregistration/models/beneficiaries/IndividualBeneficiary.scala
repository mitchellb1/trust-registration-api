package uk.gov.hmrc.trustregistration.models.beneficiaries

import play.api.libs.json.Json
import uk.gov.hmrc.trustregistration.models.Individual

case class IndividualBeneficiary(individual: Individual,
                                 isVulnerable: Boolean)

object IndividualBeneficiary {
  implicit val individualBeneficiaryFormats = Json.format[IndividualBeneficiary]
}


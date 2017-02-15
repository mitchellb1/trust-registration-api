package uk.gov.hmrc.trustregistration.models.beneficiaries

import play.api.libs.json.Json

case class UnidentifiedBeneficiary(description: String)

object UnidentifiedBeneficiary {
  implicit val unidentifiedBeneficiaryFormats = Json.format[UnidentifiedBeneficiary]
}

package uk.gov.hmrc.trustregistration.models.beneficiaries

import play.api.libs.json.Json
import uk.gov.hmrc.trustregistration.models.Address

case class OtherBeneficiary(beneficiaryDescription: String,
                            correspondenceAddress: Address)

object OtherBeneficiary {
  implicit val otherBeneficiaryFormats = Json.format[OtherBeneficiary]
}
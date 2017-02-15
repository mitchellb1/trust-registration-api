package uk.gov.hmrc.trustregistration.models.beneficiaries

import play.api.libs.json.Json
import uk.gov.hmrc.trustregistration.models.Address

case class CharityBeneficiary(charityName: String,
                              charityNumber: String,
                              correspondenceAddress: Address)

object CharityBeneficiary {
  implicit val charityBeneficiaryFormats = Json.format[CharityBeneficiary]
}

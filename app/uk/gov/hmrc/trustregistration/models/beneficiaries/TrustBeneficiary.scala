package uk.gov.hmrc.trustregistration.models.beneficiaries

import play.api.libs.json.Json
import uk.gov.hmrc.trustregistration.models.Address

case class TrustBeneficiary(trustBeneficiaryName: String,
                            trustBeneficiaryUTR: Option[String] = None,
                            correspondenceAddress: Address)

object TrustBeneficiary {
  implicit val trustBeneficiaryFormats = Json.format[TrustBeneficiary]
}

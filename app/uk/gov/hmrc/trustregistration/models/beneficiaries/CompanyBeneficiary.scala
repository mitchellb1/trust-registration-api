package uk.gov.hmrc.trustregistration.models.beneficiaries

import play.api.libs.json.Json
import uk.gov.hmrc.trustregistration.models.Company

case class CompanyBeneficiary(company: Company)

object CompanyBeneficiary {
  implicit val companyBeneficiaryFormats = Json.format[CompanyBeneficiary]
}
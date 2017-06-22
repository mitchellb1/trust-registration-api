/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Writes}
import uk.gov.hmrc.common.rest.resources.core.{Address, Company}


case class CompanyBeneficiary(company: Company,
                              incomeDistribution: IncomeDistribution)

object CompanyBeneficiary {
  implicit val companyBeneficiaryFormats = Json.format[CompanyBeneficiary]

  val writesToDes: Writes[CompanyBeneficiary] = (
    (JsPath \ "organisationName").write[String] and
      (JsPath \ "beneficiaryDiscretion").write[Boolean] and
      (JsPath \ "beneficiaryShareOfIncome").writeNullable[String] and
      (JsPath \ "identification" \ "address").write[Address](Address.writesToDes) and
      (JsPath \ "identification" \ "utr").writeNullable[String]

    ) (c => (c.company.name, c.incomeDistribution.isIncomeAtTrusteeDiscretion,
    c.incomeDistribution.shareOfIncome.map(c => c.toString),
    c.company.correspondenceAddress, c.company.referenceNumber))
}

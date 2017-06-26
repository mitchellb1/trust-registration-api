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

package uk.gov.hmrc.models.beneficiaries

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Writes}
import uk.gov.hmrc.models.{Address, Company}



case class LargeNumbersCompanyBeneficiaries(description: String,
                                            numberOfBeneficiaries: Long,
                                            company: Company,
                                            incomeDistribution: IncomeDistribution)

object LargeNumbersCompanyBeneficiaries {
  implicit val largeNumbersCompanyBeneficiaryFormats = Json.format[LargeNumbersCompanyBeneficiaries]

  val writesToDes: Writes[LargeNumbersCompanyBeneficiaries] = (
    (JsPath \ "organisationName").write[String] and
    (JsPath \ "description").write[String] and
    (JsPath \ "numberOfBeneficiary").write[String] and
      (JsPath).write[(Address, Option[String])](Beneficiaries.identificationWritesToDes) and
      (JsPath).write[IncomeDistribution](IncomeDistribution.writesToDes)

    )(lc => (lc.company.name,
    lc.description,
    lc.numberOfBeneficiaries.toString,
    (lc.company.correspondenceAddress, lc.company.referenceNumber), lc.incomeDistribution))

}

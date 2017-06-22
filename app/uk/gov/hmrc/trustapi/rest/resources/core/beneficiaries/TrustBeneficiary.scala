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

import play.api.libs.json.{JsPath, Json, Writes}
import uk.gov.hmrc.common.rest.resources.core.Address
import play.api.libs.functional.syntax._


case class TrustBeneficiary(trustBeneficiaryName: String,
                            trustBeneficiaryUTR: Option[String] = None,
                            correspondenceAddress: Address,
                            incomeDistribution: IncomeDistribution)

object TrustBeneficiary {
  implicit val trustBeneficiaryFormats = Json.format[TrustBeneficiary]

  val writesToDes: Writes[TrustBeneficiary] = (
    (JsPath \ "organisationName").write[String] and
      (JsPath).write[IncomeDistribution](IncomeDistribution.writesToDes) and
      (JsPath \ "identification" \ "address").write[Address](Address.writesToDes) and
      (JsPath \ "identification" \ "utr").writeNullable[String]
    ) (t => (t.trustBeneficiaryName,
    t.incomeDistribution,
    t.correspondenceAddress, t.trustBeneficiaryUTR))

}
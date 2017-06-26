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
import uk.gov.hmrc.models.Address


case class OtherBeneficiary(beneficiaryDescription: String,
                            correspondenceAddress: Address,
                            incomeDistribution: IncomeDistribution)

object OtherBeneficiary {
  implicit val otherBeneficiaryFormats = Json.format[OtherBeneficiary]


  val writesToDes: Writes[OtherBeneficiary] = (
      (JsPath \ "description").write[String] and
        (JsPath \ "address").write[Address](Address.writesToDes) and
        (JsPath \ "numberOfBeneficiary").writeNullable[String] and
        (JsPath).write[IncomeDistribution](IncomeDistribution.writesToDes)
    )(o => (o.beneficiaryDescription, o.correspondenceAddress, None, o.incomeDistribution)) //TODO: Mapping property numberOfBeneficiary missing
}

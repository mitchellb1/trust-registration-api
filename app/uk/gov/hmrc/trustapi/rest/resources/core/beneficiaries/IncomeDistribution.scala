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
import uk.gov.hmrc.trustapi.rest.resources.core.{ShareOfIncomeMissingException, ShareOfIncomeNotRequiredException}
import play.api.libs.functional.syntax._


case class IncomeDistribution(isIncomeAtTrusteeDiscretion: Boolean, shareOfIncome: Option[Int]) {

  val shareOfIncomeMissingForTrusteeDiscretionFalse = !isIncomeAtTrusteeDiscretion && !shareOfIncome.isDefined
  require(!shareOfIncomeMissingForTrusteeDiscretionFalse, ShareOfIncomeMissingException())

  val shareOfIncomeThereForTrusteeDiscretionTrue = isIncomeAtTrusteeDiscretion && shareOfIncome.isDefined && shareOfIncome.nonEmpty
  require(!shareOfIncomeThereForTrusteeDiscretionTrue, ShareOfIncomeNotRequiredException())
}

object IncomeDistribution {
  implicit val incomeDistributionFormats = Json.format[IncomeDistribution]

  val writesToDes: Writes[IncomeDistribution] = (
    (JsPath \ "beneficiaryDiscretion").write[Boolean] and
      (JsPath \ "beneficiaryShareOfIncome").writeNullable[String]
    ) (i => (i.isIncomeAtTrusteeDiscretion, i.shareOfIncome.map(c => c.toString)))
}

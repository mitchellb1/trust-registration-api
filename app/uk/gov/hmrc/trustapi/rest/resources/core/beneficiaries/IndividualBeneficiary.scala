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

import org.joda.time.DateTime
import play.api.libs.json.Writes.jodaDateWrites
import play.api.libs.json.{JsPath, Json, Writes}
import uk.gov.hmrc.common.rest.resources.core.Individual
import uk.gov.hmrc.common.rest.resources.core.Individual.nameWritesToDes
import play.api.libs.functional.syntax._


case class IndividualBeneficiary(individual: Individual,
                                 isVulnerable: Boolean,
                                 incomeDistribution: IncomeDistribution){
  val beneficiaryType = "NA"
}

object IndividualBeneficiary {
  implicit val individualBeneficiaryFormats = Json.format[IndividualBeneficiary]

  val writesToDes: Writes[IndividualBeneficiary] = (
    (JsPath \ "name").write[(String, Option[String], String)](nameWritesToDes) and
      (JsPath \ "dateOfBirth").write[DateTime](jodaDateWrites("YYYY-MM-DD")) and
      (JsPath \ "vulnerableBeneficiary").write[Boolean] and
      (JsPath \ "beneficiaryType").write[String] and
      (JsPath \ "beneficiaryDiscretion").write[Boolean] and
      (JsPath \ "beneficiaryShareOfIncome").writeNullable[String] and
      (JsPath \ "identification").write[Individual](Individual.identificationWritesToDes)
    ) (i => ((i.individual.givenName, i.individual.otherName, i.individual.familyName),
    i.individual.dateOfBirth,
    i.isVulnerable,
    i.beneficiaryType,
    i.incomeDistribution.isIncomeAtTrusteeDiscretion,
    i.incomeDistribution.shareOfIncome.map(c => c.toString),
    i.individual))
}

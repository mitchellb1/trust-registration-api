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

package uk.gov.hmrc.common.mapping

import uk.gov.hmrc.common.des.DesIndividualDetails
import uk.gov.hmrc.common.rest.resources.core.Individual


trait DesIndividualDetailsMap {

  def toDes(individual: Individual): DesIndividualDetails = {
    new DesIndividualDetails(
      name = DesNameMap.toDes(individual),
      dateOfBirth = individual.dateOfBirth,
      vulnerableBeneficiary = None,
      beneficiaryType = None,
      beneficiaryDiscretion = None,
      beneficiaryShareOfIncome = None,
      identification = DesIdentificationMap.toDes(individual)
    )
  }

//  def toDomain(desIndividualDetails: DesIndividualDetails): Individual = {
//    new Address(
//      line1 = address.line1,
//      line2 = Some(address.line2),
//      line3 = address.line3,
//      line4 = address.line4,
//      postalCode = address.postCode,
//      countryCode = address.country
//    )
//  }
}
object DesIndividualDetailsMap extends DesIndividualDetailsMap

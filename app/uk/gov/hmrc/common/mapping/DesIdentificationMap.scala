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

import uk.gov.hmrc.common.des._
import uk.gov.hmrc.common.rest.resources.core.Individual


trait DesIdentificationMap {

  def toDes(individual: Individual): DesIdentification = {
    new DesIdentification(
      nino = individual.nino,
      passport = None,
      address = Some(AddressMap.toDes(individual.correspondenceAddress.get))
    )
  }

  //  def toDomain(desIdentification: DesIdentification, desName: DesName, desIndividualDetails: DesIndividualDetails): Individual = {
  //
  //    new Individual(
  //      givenName = desName.firstName,
  //      familyName = desName.lastName,
  //      dateOfBirth = desIndividualDetails.dateOfBirth,
  //      otherName = desName.middleName,
  //      nino = desIdentification.nino,
  //      telephoneNumber = None,
  //      passportOrIdCard = None,
  //      correspondenceAddress = Some(AddressMap.toDomain(desIdentification.address.get))
  //    )
  //  }
}

object DesIdentificationMap extends DesIdentificationMap

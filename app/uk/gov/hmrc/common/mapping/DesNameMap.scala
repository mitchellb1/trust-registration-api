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

import uk.gov.hmrc.common.des.DesName
import uk.gov.hmrc.common.rest.resources.core.Individual


trait DesNameMap {

  def toDes(individual: Individual): DesName = {
    new DesName(
      firstName = individual.givenName,
      middleName = individual.otherName,
      lastName = individual.familyName
    )
  }

  //  def toDomain(desName: DesName,
  //               nino: Option[String] = None,
  //               telephoneNumber: Option[String] = None,
  //               desPassport: Option[DesPassportType] = None,
  //               desCorrespondenceAddress: Option[DesAddress] = None,
  //               desIndividualDetails: DesIndividualDetails): Individual = {
  //    new Individual(
  //      givenName = desName.firstName,
  //      otherName = desName.middleName,
  //      familyName = desName.lastName,
  //      nino = nino,
  //      telephoneNumber = telephoneNumber,
  //      correspondenceAddress = Some(toDomain(desCorrespondenceAddress.get)),
  //      passportOrIdCard = None,
  //      dateOfBirth = desIndividualDetails.dateOfBirth
  //    )
  //  }
}

object DesNameMap extends DesNameMap

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

package uk.gov.hmrc.common.mapping.todomain

import org.joda.time.DateTime
import uk.gov.hmrc.common.des.{DesIdentification, DesName, DesWillIdentification}
import uk.gov.hmrc.common.mapping.AddressMapper
import uk.gov.hmrc.common.rest.resources.core.Individual


object IndividualMapper {
  def toDomain(desName: DesName,
               dateOfBirth: DateTime,
               telephoneNumber: Option[String] = None,
               desIdentification: Option[DesIdentification] = None,
               desWillIdentification: Option[DesWillIdentification] = None): Individual = {

    desIdentification match {
      case Some(id) =>
        Individual(desName.firstName,
          desName.lastName,
          dateOfBirth,
          desName.middleName,
          desIdentification.flatMap(c => c.nino),
          telephoneNumber,
          desIdentification.flatMap(i => i.passport.map(p => PassportMapper.toDomain(p))),
          desIdentification.flatMap(c => c.address.map(a => AddressMapper.toDomain(a))))
      case None =>
        Individual(desName.firstName,
          desName.lastName,
          dateOfBirth,
          desName.middleName,
          desWillIdentification.flatMap(c => c.nino),
          telephoneNumber,
          correspondenceAddress = desWillIdentification.flatMap(c => c.address.map(a => AddressMapper.toDomain(a))))
    }
  }
}

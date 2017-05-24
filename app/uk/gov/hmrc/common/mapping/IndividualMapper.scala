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

import org.joda.time.DateTime
import uk.gov.hmrc.common.des.{DesIdentification, DesName}
import uk.gov.hmrc.common.rest.resources.core.Individual

object IndividualMapper {
  def toDomain(desName: DesName,
               dateOfBirth: DateTime,
               telephoneNumber: Option[String],
               identification: Option[DesIdentification]): Individual = {

    Individual(desName.firstName,
      desName.lastName,
      dateOfBirth,
      desName.middleName,
      identification.flatMap(c => c.nino),
      telephoneNumber,
      identification.flatMap(i => i.passport.map(p => PassportMapper.toDomain(p))),
      identification.flatMap(c => c.address.map(a => AddressMapper.toDomain(a))))
  }
}

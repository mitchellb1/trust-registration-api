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
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.common.des.{DesIdentification, DesPersonalRepresentative}
import uk.gov.hmrc.common.rest.resources.core.{Individual, Passport}
import uk.gov.hmrc.estateapi.rest.resources.core.PersonalRepresentative
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class PersonalRepresentativeMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  "Personal Representative Mapper" must {
    "Map fields correctly to Domain Personal Representative" when {
      "we have a correct email from DES" in {
         val output: PersonalRepresentative = PersonalRepresentativeMapper.toDomain(desPersonalRepresentative)
         output.email mustBe desPersonalRepresentative.email.get
      }
      "we have a correct phone number from DES" in {
         val output: PersonalRepresentative = PersonalRepresentativeMapper.toDomain(desPersonalRepresentative)
         output.telephoneNumber mustBe desPersonalRepresentative.phoneNumber.get
      }
      "we have a correct first name , middle name and last name from DES" in {
         val output: PersonalRepresentative = PersonalRepresentativeMapper.toDomain(desPersonalRepresentative)
         output.individual.givenName mustBe desPersonalRepresentative.name.firstName
         output.individual.familyName mustBe desPersonalRepresentative.name.lastName
         output.individual.otherName.get mustBe desPersonalRepresentative.name.middleName.get
      }
      "we have a correct date of birth from DES" in {
         val output: PersonalRepresentative = PersonalRepresentativeMapper.toDomain(desPersonalRepresentative)
         output.individual.dateOfBirth mustBe desPersonalRepresentative.dateOfBirth
      }
      "we have a correct NINO from DES" in {
         val output: PersonalRepresentative = PersonalRepresentativeMapper.toDomain(desPersonalRepresentative)
         output.individual.nino mustBe desPersonalRepresentative.identification.nino
      }
    }
  }
}

case class PersonalRepresentativeMapper()

object PersonalRepresentativeMapper extends ScalaDataExamples {
  def toDomain(desPersonalRepresentative: DesPersonalRepresentative) : PersonalRepresentative = {
        PersonalRepresentative(Individual(desPersonalRepresentative.name.firstName,
          desPersonalRepresentative.name.lastName,
          desPersonalRepresentative.dateOfBirth,
          desPersonalRepresentative.name.middleName,
          desPersonalRepresentative.identification.nino),
          desPersonalRepresentative.phoneNumber.get,desPersonalRepresentative.email.get)
  }
}

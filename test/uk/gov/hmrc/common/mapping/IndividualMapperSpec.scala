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

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class IndividualMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val output = IndividualMapper.toDomain(desName,date,Some(phoneNumber), Some(desAddress), Some(nino), Some(desPassport))

  "Individual Mapper" must {
    "Map fields correctly to Domain Individual" when {
      "we have a correct first name" in {
        output.givenName mustBe desName.firstName
      }

      "we have a correct surname" in {
        output.familyName mustBe desName.lastName
      }

      "we have a middle name and we map it to other name succesfully" in {
        output.otherName mustBe desName.middleName
      }

      "we have a date of birth" in {
        output.dateOfBirth mustBe date
      }

      "we have a telephoneNumber" in {
        output.telephoneNumber mustBe Some(phoneNumber)
      }

      "we have an address" in {
        output.correspondenceAddress.get.postalCode mustBe desAddress.postCode
      }

      "we don't have an address" in {
        val output = IndividualMapper.toDomain(desName,date,Some(phoneNumber), None, Some(nino), Some(desPassport))
        output.correspondenceAddress mustBe None
      }

      "we have a valid nino" in {
        output.nino mustBe Some(nino)
      }

      "we have a valid passport" in {
        output.passportOrIdCard.get.countryOfIssue mustBe desPassport.countryOfIssue
      }

      "we don't have a passportOrIdCard" in {
        val output = IndividualMapper.toDomain(desName,date,Some(phoneNumber), None, Some(nino), None)
        output.passportOrIdCard mustBe None
      }
    }
  }
}
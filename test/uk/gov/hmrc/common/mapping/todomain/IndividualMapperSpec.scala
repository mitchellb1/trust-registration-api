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

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.common.des.{DesIdentification, DesWillIdentification}
import uk.gov.hmrc.common.rest.resources.core.Individual
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}

class IndividualMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val output = IndividualMapper.toDomain(desName, date, Some(phoneNumber), Some(desIdentification))

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
        val identification = DesIdentification(Some(nino), Some(desPassport), None)
        val output = IndividualMapper.toDomain(desName, date, Some(phoneNumber), Some(identification))

        output.correspondenceAddress mustBe None
      }
      "we have a valid nino" in {
        val identification = DesIdentification(Some(nino), Some(desPassport), None)
        val output = IndividualMapper.toDomain(desName, date, Some(phoneNumber), Some(identification))

        output.nino mustBe Some(nino)
      }
      "we have a valid passport" in {
        output.passportOrIdCard.get.countryOfIssue mustBe desPassport.countryOfIssue
      }
      "we don't have a passportOrIdCard" in {
        val identification = DesIdentification(Some(nino), None, Some(desAddress))
        val output = IndividualMapper.toDomain(desName, date, Some(phoneNumber), Some(identification))

        output.passportOrIdCard mustBe None
      }
      "we don't have an identificiation" in {
        val output: Individual = IndividualMapper.toDomain(desName, date, Some(phoneNumber), None)

        output.passportOrIdCard mustBe None
      }
      "we have a deswill identification" in {
        val desWillId = DesWillIdentification(Some(nino),Some(desAddress))
        val output = IndividualMapper.toDomain(desName, date, Some(phoneNumber), Some(desWillId))

        output.nino mustBe Some(nino)
        output.correspondenceAddress.get.line1 mustBe desWillId.address.get.line1
      }
      "we have no identification object" in {
        val output = IndividualMapper.toDomain(desName, date, Some(phoneNumber))

        output.nino mustBe None
        output.correspondenceAddress mustBe None
        output.passportOrIdCard mustBe None
      }
    }
  }
}
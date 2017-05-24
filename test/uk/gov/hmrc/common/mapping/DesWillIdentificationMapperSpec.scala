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
import uk.gov.hmrc.common.des.{DesWillIdentification, MissingPropertyException}
import uk.gov.hmrc.common.rest.resources.core.Individual
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DesWillIdentificationMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val SUT = DesWillIdentificationMapper
  val domainIndividualPassportToMap: Individual = individual
  val domainIndividualwithNinoToMap: Individual = individualwithNino

  "Des Identification Mapper" must {
    "map properties correctly" when {
      "we have a correct Nino in the des domain" in {
        val output: DesWillIdentification = SUT.toDes(domainIndividualwithNinoToMap)
        output.nino mustBe domainIndividualwithNinoToMap.nino
      }
      "we have a no address in the des domain if we have a nino" in {
        val output: DesWillIdentification = SUT.toDes(domainIndividualwithNinoToMap)
        output.address mustBe None
      }
      "we have a no passport details in the des domain" in {
        val output: DesWillIdentification = SUT.toDes(domainIndividualwithNinoToMap)
        output.nino mustBe domainIndividualwithNinoToMap.nino
      }
      "we have a correct line 1 in the address for des domain if no nino" in {
        val output: DesWillIdentification = SUT.toDes(domainIndividualPassportToMap)
        output.address.get.line1 mustBe domainIndividualPassportToMap.correspondenceAddress.get.line1
      }
      "we have a correct line 2 in the address for des domain if no nino" in {
        val output: DesWillIdentification = SUT.toDes(domainIndividualPassportToMap)
        output.address.get.line2 mustBe domainIndividualPassportToMap.correspondenceAddress.get.line2.get
      }
      "we have a correct line 3 in the address for des domain if no nino" in {
        val output: DesWillIdentification = SUT.toDes(domainIndividualPassportToMap)
        output.address.get.line3 mustBe domainIndividualPassportToMap.correspondenceAddress.get.line3
      }
      "we have a correct line 4 in the address for des domain if no nino" in {
        val output: DesWillIdentification = SUT.toDes(domainIndividualPassportToMap)
        output.address.get.line4 mustBe domainIndividualPassportToMap.correspondenceAddress.get.line4
      }
      "we have a correct postcode for des domain if no nino" in {
        val output: DesWillIdentification = SUT.toDes(domainIndividualPassportToMap)
        output.address.get.postCode mustBe domainIndividualPassportToMap.correspondenceAddress.get.postalCode
      }
      "we have a correct country for des domain if no nino" in {
        val output: DesWillIdentification = SUT.toDes(domainIndividualPassportToMap)
        output.address.get.country mustBe domainIndividualPassportToMap.correspondenceAddress.get.countryCode
      }
      "we have no nino in the des domain if no nino" in {
        val output: DesWillIdentification = SUT.toDes(domainIndividualPassportToMap)
        output.nino mustBe None
      }
    }

    "thrown an exception" when {
      "nino and address missing" in {
        val individual = Individual(
          givenName = "Leo",
          otherName = None,
          familyName = "Spaceman",
          dateOfBirth = new DateTime("1900-01-01"),
          nino = None,
          passportOrIdCard = Some(passport),
          correspondenceAddress = None,
          telephoneNumber = None
        )
        val ex = the[MissingPropertyException] thrownBy SUT.toDes(individual)
        ex.getMessage must include("Mapping to Des error : DesWillIdentificationMapper : Individual has missing Nino and Address")
      }
    }
  }
}




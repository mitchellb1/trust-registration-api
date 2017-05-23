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
import uk.gov.hmrc.common.des.DesIndividualDetails
import uk.gov.hmrc.common.rest.resources.core.Individual
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DesIndividualDetailsMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val SUT = DesIndividualDetailsMapper
  val domainIndividualPassportToMap: Individual = individual
  val domainIndividualwithNinoToMap: Individual = individualwithNino

  "Des Identification Mapper" must {
    "map properties correctly" when {
      "we have a correct dateOfBirth in the our domain" in {
        val output: DesIndividualDetails = SUT.toDes(domainIndividualwithNinoToMap)
        output.dateOfBirth mustBe domainIndividualwithNinoToMap.dateOfBirth
      }
      "we have a name in our domain" in {
        val output: DesIndividualDetails = SUT.toDes(domainIndividualwithNinoToMap)
        output.name.lastName mustBe domainIndividualwithNinoToMap.familyName
        output.name.middleName mustBe domainIndividualwithNinoToMap.otherName
        output.name.firstName mustBe domainIndividualwithNinoToMap.givenName
      }
      "we have a no passport details in the our domain" in {
        val output: DesIndividualDetails = SUT.toDes(domainIndividualwithNinoToMap)
        output.identification.passport mustBe None
      }
//      "we have a correct line 1 in the address for des domain if no nino" in {
//        val output: DesIndividualDetails = SUT.toDes(domainIndividualPassportToMap)
//        output.address.get.line1 mustBe domainIndividualPassportToMap.correspondenceAddress.get.line1
//      }
//      "we have a correct line 2 in the address for des domain if no nino" in {
//        val output: DesIndividualDetails = SUT.toDes(domainIndividualPassportToMap)
//        output.address.get.line2 mustBe domainIndividualPassportToMap.correspondenceAddress.get.line2.get
//      }
//      "we have a correct line 3 in the address for des domain if no nino" in {
//        val output: DesIndividualDetails = SUT.toDes(domainIndividualPassportToMap)
//        output.address.get.line3 mustBe domainIndividualPassportToMap.correspondenceAddress.get.line3
//      }
//      "we have a correct line 4 in the address for des domain if no nino" in {
//        val output: DesIndividualDetails = SUT.toDes(domainIndividualPassportToMap)
//        output.address.get.line4 mustBe domainIndividualPassportToMap.correspondenceAddress.get.line4
//      }
//      "we have a correct postcode for des domain if no nino" in {
//        val output: DesIndividualDetails = SUT.toDes(domainIndividualPassportToMap)
//        output.address.get.postCode mustBe domainIndividualPassportToMap.correspondenceAddress.get.postalCode
//      }
//      "we have a correct country for des domain if no nino" in {
//        val output: DesIndividualDetails = SUT.toDes(domainIndividualPassportToMap)
//        output.address.get.country mustBe domainIndividualPassportToMap.correspondenceAddress.get.countryCode
//      }
//      "we have no nino in the des domain if no nino" in {
//        val output: DesIndividualDetails = SUT.toDes(domainIndividualPassportToMap)
//        output.nino mustBe None
//      }
    }

//    "thrown an exception" when {
//      "line 2 is not provided" in {
//        val invalidLine2ToMap: Address = Address(
//          line1 = "Line 1",
//          line2 = None ,
//          line3 = Some("Line 3"),
//          line4 = Some("Line 4"),
//          postalCode = None,
//          countryCode = "ES"
//        )
//        val declaration: Declaration = Declaration(
//          invalidLine2ToMap,
//          true: Boolean,
//          "Joe",
//          "Bloggs",
//          new DateTime("1940-03-31"),
//          None)
//        val ex = the[MissingPropertyException] thrownBy SUT.toDes(declaration)
//        ex.getMessage must include("Missing address line 2")
//      }
//    }
  }
}




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
import uk.gov.hmrc.common.des.DesIdentification
import uk.gov.hmrc.common.rest.resources.core.Individual
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DesIdentificationMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val SUT = DesIdentificationMapper
  val domainIndividualToMap: Individual = individual
  val desIdentificationToMap: DesIdentification = desIdentification


  "Des Identification Mapper" must {
//    "map properties correctly" when {
//      "we have a correct first name in the des domain" in {
//        val output: DesIdentification = SUT.toDes(domainIndividualToMap)
//        output.nino mustBe domainIndividualToMap.givenName
//      }
//      "we have a correct middle in the des domain" in {
//        val output: DesIdentification = SUT.toDes(domainIndividualToMap)
//        output.passport mustBe domainIndividualToMap.otherName
//      }
//      "we have a correct last in the des domain" in {
//        val output: DesIdentification = SUT.toDes(domainIndividualToMap)
//        output.name.lastName mustBe domainIndividualToMap.familyName
//      }
//      "we have a correct line 1 in the address for des domain" in {
//        val output: DesIdentification = SUT.toDes(domainIndividualToMap)
//        output.address.line1 mustBe domainIndividualToMap.correspondenceAddress.line1
//      }
//      "we have a correct line 2 in the address for des domain" in {
//        val output: DesIdentification = SUT.toDes(domainIndividualToMap)
//        output.address.line2 mustBe domainIndividualToMap.correspondenceAddress.line2.get
//      }
//      "we have a correct line 3 in the address for des domain" in {
//        val output: DesIdentification = SUT.toDes(domainIndividualToMap)
//        output.address.line3 mustBe domainIndividualToMap.correspondenceAddress.line3
//      }
//      "we have a correct line 4 in the address for des domain" in {
//        val output: DesIdentification = SUT.toDes(domainIndividualToMap)
//        output.address.line4 mustBe domainIndividualToMap.correspondenceAddress.line4
//      }
//      "we have a correct postcode for des domain" in {
//        val output: DesIdentification = SUT.toDes(domainIndividualToMap)
//        output.address.postCode mustBe domainIndividualToMap.correspondenceAddress.postalCode
//      }
//      "we have a correct country for des domain" in {
//        val output: DesIdentification = SUT.toDes(domainIndividualToMap)
//        output.address.country mustBe domainIndividualToMap.correspondenceAddress.countryCode
//      }
//    }

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




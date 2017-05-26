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

package uk.gov.hmrc.common.mapping.todes

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.common.des.DesCorrespondence
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}



class DesCorrespondenceSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val SUT = DesCorrespondenceMapper

  val domainEstateToMap = validEstateWithPersonalRepresentative


  "DesCorrespondenceMapper toDes" must {
    "map properties correctly" when {
      "we have a correct abroadIndicator on the domain" in {
        val output: DesCorrespondence = SUT.toDes(domainEstateToMap)
        output.abroadIndicator
      }

      "we have a correct name" in {
        val output = SUT.toDes(domainEstateToMap)
        output.name mustBe "Test Estate"
      }

      "we have a valid domain address" in {
        val output = SUT.toDes(domainEstateToMap)

        output.address.line1 mustBe domainEstateToMap.correspondenceAddress.line1
        output.address.line2 mustBe domainEstateToMap.correspondenceAddress.line2.get
        output.address.line3 mustBe domainEstateToMap.correspondenceAddress.line3
        output.address.line4 mustBe domainEstateToMap.correspondenceAddress.line4
        output.address.postCode mustBe domainEstateToMap.correspondenceAddress.postalCode
        output.address.country mustBe domainEstateToMap.correspondenceAddress.countryCode
      }

      "we have a phone number" in {
        val output: DesCorrespondence = SUT.toDes(domainEstateToMap)
        output.phoneNumber mustBe "0191 365 1234"
      }
    }
  }
}

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
import uk.gov.hmrc.common.des.{DesAddress, MissingPropertyException}
import uk.gov.hmrc.common.rest.resources.core.Address
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}

class AddressMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val SUT = AddressMapper
  val domainAddressToMap = address
  val desAddressToMap = desAddress

  "Address mapper to Domain" must {
    "map properties correctly" when {
      "we have a correct line 1 on DES" in {
        val output: Address = SUT.toDomain(desAddressToMap)
        output.line1 mustBe desAddressToMap.line1
      }
      "we have a correct line 2 on DES" in {
        val output: Address = SUT.toDomain(desAddressToMap)
        output.line2.get mustBe desAddressToMap.line2
      }
      "we have a correct line 3 on DES" in {
        val output: Address = SUT.toDomain(desAddressToMap)
        output.line3 mustBe desAddressToMap.line3
      }
      "we have a correct line 4 on DES" in {
        val output: Address = SUT.toDomain(desAddressToMap)
        output.line4 mustBe desAddressToMap.line4
      }
      "we have a correct postcode on DES" in {
        val output: Address = SUT.toDomain(desAddressToMap)
        output.postalCode mustBe desAddressToMap.postCode
      }
      "we have a correct country on DES" in {
        val output = SUT.toDomain(desAddressToMap)
        output.countryCode mustBe desAddressToMap.country
      }
    }
  }

  "Address mapper to Des" must {
    "map properties correctly" when {
      "we have a correct line 1 on our domain" in {
        val output: DesAddress = SUT.toDes(address)
        output.line1 mustBe domainAddressToMap.line1
      }
      "we have a correct line 2  on our domain" in {
        val output = SUT.toDes(address)
        output.line2 mustBe domainAddressToMap.line2.get
      }
      "we have a correct line 3  on our domain" in {
        val output = SUT.toDes(address)
        output.line3 mustBe domainAddressToMap.line3
      }
      "we have a correct line 4 on our domain" in {
        val output = SUT.toDes(address)
        output.line4 mustBe domainAddressToMap.line4
      }
      "we have a correct postcode on our domain" in {
        val output = SUT.toDes(address)
        output.postCode mustBe domainAddressToMap.postalCode
      }
      "we have a correct country on our domain" in {
        val output = SUT.toDes(address)
        output.country mustBe domainAddressToMap.countryCode
      }
    }
    "thrown an exception" when {
      "line 2 is not provided" in {
        val invalidLine2ToMap = Address(
          line1 = "Line 1",
          line2 = None,
          line3 = Some("Line 3"),
          line4 = Some("Line 4"),
          postalCode = None,
          countryCode = "ES"
        )
        val ex = the[MissingPropertyException] thrownBy SUT.toDes(invalidLine2ToMap)
        ex.getMessage must include("Missing address line 2")
      }
    }
  }
}
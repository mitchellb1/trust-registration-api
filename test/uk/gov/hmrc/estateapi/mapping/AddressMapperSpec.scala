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

package uk.gov.hmrc.estateapi.mapping

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.common.des.{DesAddress, MissingPropertyException}
import uk.gov.hmrc.common.mapping.AddressMapper
import uk.gov.hmrc.common.rest.resources.core.Address
import uk.gov.hmrc.utils.ScalaDataExamples


class AddressMapperSpec extends PlaySpec
  with OneAppPerSuite with ScalaDataExamples {

  val mapper = new AddressMapper()
  val addressToMap = address


  "Address mapper" must {
    "map properties correctly" when {
      "we have a correct line 1 on our domain" in {
        val output: DesAddress = mapper.toDes(address)
        output.line1 mustBe addressToMap.line1
      }
      "we have a correct line 2  on our domain" in {
        val output = mapper.toDes(address)
        output.line2 mustBe addressToMap.line2.get
      }
      "we have a correct line 3  on our domain" in {
        val output = mapper.toDes(address)
        output.line3 mustBe addressToMap.line3
      }
      "we have a correct line 4 on our domain" in {
        val output = mapper.toDes(address)
        output.line4 mustBe addressToMap.line4
      }
      "we have a correct postcode on our domain" in {
        val output = mapper.toDes(address)
        output.postCode mustBe addressToMap.postalCode
      }
      "we have a correct country on our domain" in {
        val output = mapper.toDes(address)
        output.country mustBe addressToMap.countryCode
      }
    }

    "thrown an exception" when {
      "line 2 is not provided" in {
        val invalidLine2ToMap = Address(
          line1 = "Line 1",
          line2 = None ,
          line3 = Some("Line 3"),
          line4 = Some("Line 4"),
          postalCode = None,
          countryCode = "ES"
        )
        val ex = the[MissingPropertyException] thrownBy mapper.toDes(invalidLine2ToMap)
        ex.getMessage must include("Missing address line 2")
      }
    }
  }
}




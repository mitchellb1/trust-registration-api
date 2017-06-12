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

package uk.gov.hmrc.trustregistration.models

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsPath, JsString, Json, Writes}
import uk.gov.hmrc.common.rest.resources.core.Address
import uk.gov.hmrc.utils.{JsonExamples, ScalaDataExamples}
import play.api.libs.functional.syntax._

class  AddressSpec extends PlaySpec with JsonExamples with ScalaDataExamples {

  val addressGb = """{"addressGB": {"line1": "123 Easy Street", "postalCode": "AB1 1AB", "countryCode":"GB"}}"""
  val addressNonGb = """{"addressNonGB": {"line1": "123 Easy Street", "countryCode":"ES"}}"""
  val addressLegacy = """{"line1": "123 Easy Street", "countryCode":"ES"}""" // TODO: Take this out once we've ported over to new address style

  "Address" must {
    "serialize from Json" when {
      "a GB address is specified" in {
        val address: Address = Json.parse(addressGb).as[Address]

        address.line1 mustBe "123 Easy Street"
        address.postalCode mustBe Some("AB1 1AB")
      }
      "a non GB address is specified" in {
        val address: Address = Json.parse(addressNonGb).as[Address]

        address.line1 mustBe "123 Easy Street"
        address.postalCode.isDefined mustBe false
      }
      "a old-style address is specified (i.e. backwards compatibility!)" in {
        val address: Address = Json.parse(addressLegacy).as[Address]

        address.line1 mustBe "123 Easy Street"
        address.postalCode.isDefined mustBe false
      }
    }

    "convert to a valid DES Address JSON body" when {
      "we have a full address" in {
        val address = Address(
          line1 = "Line 1",
          line2 = Some("Line 2"),
          line3 = Some("Line 3"),
          line4 = Some("Line 4"),
          postalCode = Some("Test"),
          countryCode = "GB"
        )


       val addressWrites : Writes[Address] = (
            (JsPath \ "line1").write[String] and
            (JsPath \ "line2").writeNullable[String] and
            (JsPath \ "line3").writeNullable[String] and
            (JsPath \ "line4").writeNullable[String] and
            (JsPath \ "postCode").writeNullable[String] and
            (JsPath \ "country").write[String]
            )(unlift(Address.unapply))



        val json = Json.toJson(address)(addressWrites)

        (json \ "line1").get mustBe JsString(address.line1)
        (json \ "line2").get mustBe JsString(address.line2.get)
        (json \ "line3").get mustBe JsString(address.line3.get)
        (json \ "line4").get mustBe JsString(address.line4.get)
        (json \ "postCode").get mustBe JsString(address.postalCode.get)
        (json \ "country").get mustBe JsString(address.countryCode)
      }
    }

    "throw an exception" when {
      "the country code is GB and the postcode is Empty" in {
        val ex = the [IllegalArgumentException] thrownBy Address("test",None,None,None,Some(""),"GB")
        ex.getMessage must include("missing field")
      }

      "the country code is GB and the postcode is None" in {
        val ex = the [IllegalArgumentException] thrownBy Address("test",None,None,None,None,"GB")
        ex.getMessage must include("missing field")
      }

      "the country code is not GB and there is a postcode" in {
        val ex = the [IllegalArgumentException] thrownBy Address("test",None,None,None,Some("NE40 4US"),"ES")
        ex.getMessage must include("not required field")
      }

      "the country code is not GB and there is an empty string for postcode" in {
        val ex = the [IllegalArgumentException] thrownBy Address("test",None,None,None,Some(""),"ES")
        ex.getMessage must include("not required field")
      }
    }
  }

}

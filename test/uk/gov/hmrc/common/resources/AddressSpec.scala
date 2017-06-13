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
import play.api.libs.json._
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

    "convert from a valid DES JSON body " when {
      "we have a full address" in {
        val address = Json.parse("""{"line1" : "Test", "line2" : "Test2", "line3" : "Test3", "line4" : "Test4", "postCode" : "WN1 2TT", "country" : "GB"}""")

        val output = address.validate[Address](Address.readsFromDes).get

        output.line1 mustBe (address \ "line1").as[String]
        output.line2.get mustBe (address \ "line2").as[String]
        output.line3.get mustBe (address \ "line3").as[String]
        output.line4.get mustBe (address \ "line4").as[String]
        output.postalCode.get mustBe (address \ "postCode").as[String]
        output.countryCode mustBe (address \ "country").as[String]
      }


      "we have only required fields " in {
        val address = Json.parse("""{"line1" : "line1Address", "line2" : "line2Address", "postCode" : "NE1 111", "country" : "GB"}""")
        val output = address.validate[Address](Address.readsFromDes).get

        output.line1 mustBe "line1Address"
        output.line2.get mustBe "line2Address"
        output.line3 mustBe None
        output.line4 mustBe None
        output.postalCode.get mustBe  "NE1 111"
        output.countryCode mustBe "GB"
      }


    }

    "convert to a valid DES Address JSON body" when {
      "we have a full address" in {
        val gbAddress = address.copy(countryCode = "GB",postalCode = Some("Test"))

        val json = Json.toJson(gbAddress)(Address.writesToDes)

        (json \ "line1").get mustBe JsString(gbAddress.line1)
        (json \ "line2").get mustBe JsString(gbAddress.line2.get)
        (json \ "line3").get mustBe JsString(gbAddress.line3.get)
        (json \ "line4").get mustBe JsString(gbAddress.line4.get)
        (json \ "postCode").get mustBe JsString(gbAddress.postalCode.get)
        (json \ "country").get mustBe JsString(gbAddress.countryCode)
      }

      "we have a full valid address with missing optional properties" in {
        val address = Address(
          line1 = "Line 1",
          line2 = None,
          line3 = None,
          line4 = None,
          postalCode = Some("Test"),
          countryCode = "GB"
        )

        val json = Json.toJson(address)(Address.writesToDes)

        json.toString() mustNot  include("line2")
        json.toString() mustNot  include("line3")
        json.toString() mustNot  include("line4")
      }

      "we have a foreign address" in {
        val json = Json.toJson(address)(Address.writesToDes)

        (json \ "line1").get mustBe JsString(address.line1)
        (json \ "line2").get mustBe JsString(address.line2.get)
        (json \ "line3").get mustBe JsString(address.line3.get)
        (json \ "line4").get mustBe JsString(address.line4.get)
        (json \ "country").get mustBe JsString(address.countryCode)
        json.toString() mustNot  include("postCode")
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

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

package uk.gov.hmrc.trustapi.rest.mapping


import org.scalatestplus.play.PlaySpec
import play.api.libs.json._
import uk.gov.hmrc.common.rest.resources.core.Address
import uk.gov.hmrc.trustapi.rest.resources.core.Trust
import uk.gov.hmrc.utils.ScalaDataExamples


class JsonMapperSpec extends PlaySpec with ScalaDataExamples {

  //Domain to DES custom writes

  val trustWrites = new Writes[Trust] {
    def writes(trust: Trust) = {
      val address = Json.toJson(trust.correspondenceAddress)(Address.writesToDes)

      Json.obj("correspondence" -> Json.obj(
        "name" -> JsString(trust.name),
        "phoneNumber" -> JsString(trust.telephoneNumber),
        "address" -> address))
    }
  }




  "TrustToDesWrites" should {
    "Convert the domain representation of an Employment Trust to a DES schema valid JSON body" when {
      "The trust has a valid name" in {
        val domainTrust = trustWithEmploymentTrust

        val json: JsValue = Json.toJson(domainTrust)(trustWrites)

        (json \ "correspondence" \ "name").get mustBe JsString("Test Trust")
      }
      "The trust has a valid phoneNumber" in {
        val domainTrust = trustWithEmploymentTrust

        val json: JsValue = Json.toJson(domainTrust)(trustWrites)

        (json \ "correspondence" \ "phoneNumber").get mustBe JsString("0044 1234 1234")
      }
      "The trust has a valid address" in {
        val domainTrust = trustWithEmploymentTrust

        val json: JsValue = Json.toJson(domainTrust)(trustWrites)

        (json \ "correspondence" \ "address" \ "line1").get mustBe JsString("Line 1")
      }
    }
  }
}

/*

{
  "correspondence": {
    "abroadIndicator": true,
    "name": "Test Trust",
    "address": {
      "line1": "Line 1",
      "line2": "Line 2",
      "line3": "Line 3",
      "line4": "Line 4",
      "country": "ES"
    },
    "phoneNumber": "0044 1234 1234"
  },
  "yearsReturns": {
    "taxReturnsNoDues": true
  },
  "declaration": {
    "name": {
      "firstName": "joe",
      "lastName": "Blogs"
    },
    "address": {
      "line1": "weqr",
      "line2": "erqw",
      "country": "GB"
    }
  },
  "details": {
    "trust": {
      "details": {
        "startDate": "1900-01-05",
        "lawCountry": "ES",
        "administrationCountry": "ES",
        "typeOfTrust": "Will Trust or Intestacy Trust"
      },
      "entities": {
        "beneficiary": {},
        "leadTrustees": {
          "name": "some company",
          "phoneNumber": "01",
          "identification": {
            "utr": "32532"
          },
          "email": ""
        },
        "settlors": {}
      },
      "assets": {}
    }
  }
}
 */

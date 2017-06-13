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

      Json.obj("correspondence" -> Json.obj(
        "abroadIndicator" -> JsBoolean(trust.correspondenceAddress.countryCode != "GB"),
        "name" -> JsString(trust.name),
        "phoneNumber" -> JsString(trust.telephoneNumber),
        "address" -> Json.toJson(trust.correspondenceAddress)(Address.writesToDes)))
    }
  }




  "TrustToDesWrites" should {
    "Convert the domain representation of an Employment Trust to a DES schema valid JSON body" when {
      val domainTrust = trustWithEmploymentTrust
      val json: JsValue = Json.toJson(domainTrust)(trustWrites)
      "The trust has a valid name" in {
        (json \ "correspondence" \ "name").get mustBe JsString("Test Trust")
      }
      "The trust has a valid phoneNumber" in {
        (json \ "correspondence" \ "phoneNumber").get mustBe JsString("0044 1234 1234")
      }
      "The trust has a valid address" in {
        (json \ "correspondence" \ "address" \ "line1").get mustBe JsString("Line 1")
      }
      "We have an abroad indicator" in {
        (json \ "correspondence" \ "abroadIndicator").get mustBe JsBoolean(true)
      }
    }
  }
}
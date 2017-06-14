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

import org.scalatestplus.play.PlaySpec
import play.api.libs.json._
import uk.gov.hmrc.common.rest.resources.core.{Address, Declaration, YearReturn, YearsOfTaxConsequence}
import uk.gov.hmrc.trustapi.rest.resources.core.Trust
import uk.gov.hmrc.utils.ScalaDataExamples



class DesRequestJsonMapperSpec extends PlaySpec with ScalaDataExamples {

  val trustWrites = new Writes[Trust] {
    def writes(trust: Trust) = {
      JsObject(
        Map("correspondence" -> Json.obj(
          "abroadIndicator" -> JsBoolean(trust.correspondenceAddress.countryCode != "GB"),
          "name" -> JsString(trust.name),
          "phoneNumber" -> JsString(trust.telephoneNumber),
          "address" -> Json.toJson(trust.correspondenceAddress)(Address.writesToDes)),
          "declaration" -> Json.toJson(trust.declaration)(Declaration.writesToDes)) ++
          optAdminAttribute("admin", trust.utr) ++
          trust.yearsOfTaxConsequence.map(v => ("yearsReturns",Json.toJson(v)))
      )
    }
    def optAdminAttribute(s: String, option: Option[String]) = {
      option.map(value => (s, Json.obj(
        "utr" -> JsString(value)
      )))
    }
  }

  "TrustToDesWrites" should {
    "Convert the domain representation of an Employment Trust to a DES schema valid JSON body" when {
      val domainTrust = trustWithEmploymentTrust
      val json: JsValue = Json.toJson(domainTrust)(trustWrites)

      "The trust has a valid name" in {
        (json \ "correspondence" \ "name").get mustBe JsString(domainTrust.name)
      }
      "The trust has a valid phoneNumber" in {
        (json \ "correspondence" \ "phoneNumber").get mustBe JsString(domainTrust.telephoneNumber)
      }
      "The trust has a valid address" in {
        (json \ "correspondence" \ "address" \ "line1").get mustBe JsString(domainTrust.correspondenceAddress.line1)
      }
      "We have an abroad indicator" in {
        (json \ "correspondence" \ "abroadIndicator").get mustBe JsBoolean(domainTrust.correspondenceAddress.countryCode != "GB")
      }
      "The trust has a UTR" in {
        val domainTrust = trustWithEmploymentTrust.copy(utr = Some("ASDFAJSDFANSD"))
        val json: JsValue = Json.toJson(domainTrust)(trustWrites)
        (json \ "admin" \ "utr").get mustBe JsString(domainTrust.utr.get)
      }
      "There is no UTR" in {
        json.toString() mustNot include("admin")
      }
      "we have years returns with taxreturnnodues flag" in {
        val domainTrust = trustWithEmploymentTrust.copy(yearsOfTaxConsequence = Some(YearsOfTaxConsequence(Some(false))))
        val json: JsValue = Json.toJson(domainTrust)(trustWrites)
        (json \ "yearsReturns" \ "taxReturnsNoDues").get.asOpt[Boolean] mustBe domainTrust.yearsOfTaxConsequence.get.taxReturnsNoDues
      }

      "years returns has retuns information for two years" in {
        val domainTrust = trustWithEmploymentTrust.copy(yearsOfTaxConsequence = Some(YearsOfTaxConsequence(Some(false), Some(List(YearReturn("16", false),YearReturn("15", true))))))
        val json: JsValue = Json.toJson(domainTrust)(trustWrites)

        (json \ "yearsReturns" \ "returns").get.as[List[YearReturn]] mustBe domainTrust.yearsOfTaxConsequence.get.returns.get
      }

      "we don't have years returns" in {
        val domainTrust = trustWithEmploymentTrust.copy(yearsOfTaxConsequence = None)
        val json: JsValue = Json.toJson(domainTrust)(trustWrites)
        json.toString() mustNot include("yearsReturns")
      }
    }
  }
}
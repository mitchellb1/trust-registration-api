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

import org.joda.time.DateTime
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.common.rest.resources.core.Individual
import uk.gov.hmrc.utils.ScalaDataExamples


class IdentificationMapperSpec extends PlaySpec with OneAppPerSuite with ScalaDataExamples {
  "Des Identification Type" should {
    "Map to a valid JSON payload from an Individual" when {
      val domainIndividual = individual
      val json: JsValue = Json.toJson(domainIndividual)(Individual.identificationWritesToDes)

      "we have a nino" in {
        val domainIndividual = individual.copy(nino = Some("test"))
        val json: JsValue = Json.toJson(domainIndividual)(Individual.identificationWritesToDes)

        (json \ "nino").get.as[String] mustBe domainIndividual.nino.get
      }

      "we don't have a nino" in {
        (json \ "nino").validate[String].isError mustBe true
      }

      "we don't have a nino but we have a passport" in {
        (json \ "passport" \ "number").get.as[String] mustBe domainIndividual.passportOrIdCard.get.referenceNumber
        (json \ "passport" \ "expirationDate").get.as[DateTime] mustBe domainIndividual.passportOrIdCard.get.expiryDate
        (json \ "passport" \ "countryOfIssue").get.as[String] mustBe domainIndividual.passportOrIdCard.get.countryOfIssue
      }

      "we don't have a nino but we have an address" in {
        (json \ "address" \ "line1").get.as[String] mustBe domainIndividual.correspondenceAddress.get.line1
      }

      "we don't have an address" in {
        val domainIndividual = individual.copy(correspondenceAddress = None)
        val json: JsValue = Json.toJson(domainIndividual)(Individual.identificationWritesToDes)

        (json \ "address").validate[String].isError mustBe true
      }

      "we don't have a passport or address but we have a nino" in {
        val domainIndividual = individual.copy(correspondenceAddress = None,passportOrIdCard = None,nino = Some("Test"))
        val json: JsValue = Json.toJson(domainIndividual)(Individual.identificationWritesToDes)

        (json \ "address").validate[String].isError mustBe true
        (json \ "address").validate[String].isError mustBe true
        (json \ "nino").get.as[String] mustBe domainIndividual.nino.get
      }

      "we have a passport and address" in {
        (json \ "passport" \ "number").get.as[String] mustBe domainIndividual.passportOrIdCard.get.referenceNumber
        (json \ "address" \ "line1").get.as[String] mustBe domainIndividual.correspondenceAddress.get.line1
      }
    }
  }
}

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

package uk.gov.hmrc.trustapi.mapping

import org.joda.time.DateTime
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.{JsString, JsValue, Json}
import uk.gov.hmrc.trustapi.rest.resources.core.Trust
import uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries.{Beneficiaries, IndividualBeneficiary}
import uk.gov.hmrc.trustapi.rest.resources.core.trusttypes.{EmploymentTrust, TrustType}
import uk.gov.hmrc.utils.ScalaDataExamples



class TrustDetailsMapperSpec extends PlaySpec with OneAppPerSuite with ScalaDataExamples{

  "A domain representation of a Trust" should {
    "map its properties to a JSON Des representation of Trust Details" when {

      val domainTrust = trustWithEmploymentTrust
      val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)

      "we have a trust commencement date" in {
        (json \ "details" \ "trust" \ "details" \ "startDate").get.as[DateTime] mustBe domainTrust.commencementDate
      }

      "we have a trust law country" in {
        (json \ "details" \ "trust" \ "details" \ "lawCountry").get.as[String] mustBe domainTrust.legality.governingCountryCode
      }

      "we have an administration country" in {
        (json \ "details" \ "trust" \ "details" \ "administrationCountry").get.as[String] mustBe domainTrust.legality.administrationCountryCode.get
      }

      "trust have no administration country" in {
        val domainTrust = trustWithEmploymentTrust.copy(legality= legality.copy(administrationCountryCode = None))
        val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)
        (json \ "details" \ "trust" \ "details" \ "administrationCountry").validate[JsValue].isError mustBe true
      }

      "we have a uk resident with scottishLaw flag" in {
        (json \ "details" \ "trust" \ "details" \ "residentialStatus" \ "uk" \ "scottishLaw").get.as[Boolean] mustBe domainTrust.legality.isEstablishedUnderScottishLaw
      }

      "we have a uk resident with preoffshore country code details." in {
        val domainTrust = trustWithEmploymentTrust.copy(legality= legality.copy(previousOffshoreCountryCode = Some("IT")))
        val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)
        (json \ "details" \ "trust" \ "details" \ "residentialStatus" \ "uk" \ "preOffShore").get.as[String] mustBe domainTrust.legality.previousOffshoreCountryCode.get
      }

      "we have a uk resident with no preoffshore country code details." in {
        (json \ "details" \ "trust" \ "details" \ "residentialStatus" \ "uk" \ "preOffShore").validate[JsString].isError mustBe true
      }

      "we have a non uk resident with sch5atcgga92 flag" in {
        val domainTrust = trustWithEmploymentTrust.copy(isTrustUkResident = false)
        val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)
        (json \ "details" \ "trust" \ "details" \ "residentialStatus" \ "nonUK" \ "sch5atcgga92").get.as[Boolean] mustBe true //TODO: Mapping property sch5atcgga92 missing
      }

      "we have the rest of non uk resident properties" in {
        val domainTrust = trustWithEmploymentTrust.copy(isTrustUkResident = false)
        val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)

        (json \ "details" \ "trust" \ "details" \ "residentialStatus" \ "nonUK" \ "sch5atcgga92").get.as[Boolean] mustBe true //TODO: Mapping property s218ihta84 missing
        (json \ "details" \ "trust" \ "details" \ "residentialStatus" \ "nonUK" \ "agentS218IHTA84").get.as[Boolean] mustBe true //TODO: Mapping property agentS218IHTA84 missing
        (json \ "details" \ "trust" \ "details" \ "residentialStatus" \ "nonUK" \ "trusteeStatus").get.as[String] mustBe "Non Resident Domiciled" //TODO: Mapping property trusteeStatus missing
      }

      "we have a type of trust" in {
        (json \ "details" \ "trust" \ "details" \ "typeOfTrust").get.as[String] mustBe domainTrust.trustType.currentTrustType
      }

      "we have no deed of variation" in {
        (json \ "details" \ "trust" \ "details" \ "deedOfVariation").validate[JsString].isError mustBe true
      }

      "we have deed of variation for InterVivoTrust dovTypeReplace" in {
        val domainTrust = trustWithInterVivoTrustDOV1
        val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)
        (json \ "details" \ "trust" \ "details" \ "deedOfVariation").as[String] mustBe domainTrust.trustType.deedOfVariation.get
      }

      "we have deed of variation for InterVivoTrust dovTypeAbsolute" in {
        val domainTrust = trustWithInterVivoTrustDOV
        val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)
        (json \ "details" \ "trust" \ "details" \ "deedOfVariation").as[String] mustBe domainTrust.trustType.deedOfVariation.get
      }

      "we have deed of variation trust type WillIntestacyTrust" in {
        val domainTrust = trustWithWillIntestacyTrustDOV
        val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)
        (json \ "details" \ "trust" \ "details" \ "deedOfVariation").as[String] mustBe domainTrust.trustType.deedOfVariation.get
      }


      "we have interVivos of trust which is false " in {
        val domainTrust = trustWithWillIntestacyTrustDOV
        val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)
        (json \ "details" \ "trust" \ "details" \ "interVivos").as[Boolean] mustBe false
      }

      "we have interVivos of trust which is true" in {
        val domainTrust = trustWithInterVivoTrustDOV1
        val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)
        (json \ "details" \ "trust" \ "details" \ "interVivos").as[Boolean] mustBe true
      }

      "we have an efrbsStartDate" in {
        (json \ "details" \ "trust" \ "details" \ "efrbsStartDate").get.asOpt[DateTime] mustBe domainTrust.trustType.employmentTrust.get.employerFinancedRetirementBenefitSchemeStartDate
      }

      "we don't have an efrbsStartDate" in {
        val employmentTrust = Some(EmploymentTrust(assets,Beneficiaries(Some(List(IndividualBeneficiary(individual,false, incomeDistribution)))),None,None))
        val domainTrust = trustWithEmploymentTrust.copy(trustType = TrustType(employmentTrust = employmentTrust))
        val json: JsValue = Json.toJson(domainTrust)(Trust.trustWrites)
        (json \ "details" \ "trust" \ "details" \ "efrbsStartDate").validate[JsString].isError mustBe true
      }
    }
  }

}

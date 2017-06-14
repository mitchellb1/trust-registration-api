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
import play.api.libs.functional.syntax._
import org.joda.time.DateTime
import org.scalatestplus.play.PlaySpec
import play.api.libs.json._
import uk.gov.hmrc.common.rest.resources.core.{Address, Declaration, YearReturn, YearsOfTaxConsequence}
import uk.gov.hmrc.trustapi.rest.resources.core.Trust
import uk.gov.hmrc.utils.ScalaDataExamples



class DesRequestJsonMapperSpec extends PlaySpec with ScalaDataExamples {

  val trustDetailsToDesUkWrites: Writes[Trust] = (
      commonDetails and
      (JsPath \ "residentialStatus" \ "uk" \ "scottishLaw").write[Boolean] and
      (JsPath \ "residentialStatus" \ "uk" \ "preOffShore").writeNullable[String]
    )(trustDetails =>  (
        trustDetails.commencementDate,
        trustDetails.legality.governingCountryCode,
        trustDetails.legality.administrationCountryCode,
        trustDetails.trustType.currentTrustType,
        trustDetails.trustType.deedOfVariation,
        trustDetails.legality.isEstablishedUnderScottishLaw,
        trustDetails.legality.previousOffshoreCountryCode
      ))


  val trustDetailsToDesNonUkResidentWrites: Writes[Trust] = (
    commonDetails and
      (JsPath \ "residentialStatus" \ "nonUK" \ "sch5atcgga92").write[Boolean] and
      (JsPath \ "residentialStatus" \ "nonUK" \ "s218ihta84").writeNullable[Boolean] and
      (JsPath \ "residentialStatus" \ "nonUK" \ "agentS218IHTA84").writeNullable[Boolean] and
      (JsPath \ "residentialStatus" \ "nonUK" \ "trusteeStatus").writeNullable[String]
    )(trustDetails =>  (
    trustDetails.commencementDate,
    trustDetails.legality.governingCountryCode,
    trustDetails.legality.administrationCountryCode,
    trustDetails.trustType.currentTrustType,
    trustDetails.trustType.deedOfVariation,
    true, //TODO: Mapping property sch5atcgga92 missing
    Some(true), //TODO: Mapping property s218ihta84 missing
    Some(true), //TODO: Mapping property agentS218IHTA84 missing
    Some("Non Resident Domiciled") //TODO: Mapping property trusteeStatus missing
  ))

  private def commonDetails = {
      (JsPath \ "startDate").write[DateTime] and
      (JsPath \ "lawCountry").write[String] and
      (JsPath \ "administrationCountry").writeNullable[String] and
      (JsPath \ "typeOfTrust").write[String] and
      (JsPath \ "deedOfVariation").writeNullable[String]
  }

  val trustWrites = new Writes[Trust] {
    def writes(trust: Trust) = {
      JsObject(
        Map("correspondence" -> Json.obj(
          "abroadIndicator" -> JsBoolean(trust.correspondenceAddress.countryCode != "GB"),
          "name" -> JsString(trust.name),
          "phoneNumber" -> JsString(trust.telephoneNumber),
          "address" -> Json.toJson(trust.correspondenceAddress)(Address.writesToDes)),
          "declaration" -> Json.toJson(trust.declaration)(Declaration.writesToDes),
          "details" -> Json.obj(
            "trust"-> Json.obj(
              "details"-> Json.toJson(trust)(if (trust.isTrustUkResident) trustDetailsToDesUkWrites else trustDetailsToDesNonUkResidentWrites)))) ++
          trust.utr.map(v => ("admin", Json.obj("utr" -> JsString(v)))) ++
          trust.yearsOfTaxConsequence.map(v => ("yearsReturns",Json.toJson(v)))

      )
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
        (json \ "admin" ).validate[JsObject].isError  mustBe true
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
        (json \ "yearsReturns" ).validate[JsObject].isError  mustBe true
      }

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
        val json: JsValue = Json.toJson(domainTrust)(trustWrites)
        (json \ "details" \ "trust" \ "details" \ "administrationCountry").validate[JsValue].isError mustBe true
      }

      "we have a uk resident with scottishLaw flag" in {
        (json \ "details" \ "trust" \ "details" \ "residentialStatus" \ "uk" \ "scottishLaw").get.as[Boolean] mustBe domainTrust.legality.isEstablishedUnderScottishLaw
      }

      "we have a uk resident with preoffshore country code details." in {
        val domainTrust = trustWithEmploymentTrust.copy(legality= legality.copy(previousOffshoreCountryCode = Some("IT")))
        val json: JsValue = Json.toJson(domainTrust)(trustWrites)
        (json \ "details" \ "trust" \ "details" \ "residentialStatus" \ "uk" \ "preOffShore").get.as[String] mustBe domainTrust.legality.previousOffshoreCountryCode.get
      }

      "we have a uk resident with no preoffshore country code details." in {
        (json \ "details" \ "trust" \ "details" \ "residentialStatus" \ "uk" \ "preOffShore").validate[JsString].isError mustBe true
      }

      "we have a non uk resident with sch5atcgga92 flag" in {
        val domainTrust = trustWithEmploymentTrust.copy(isTrustUkResident = false)
        val json: JsValue = Json.toJson(domainTrust)(trustWrites)
        (json \ "details" \ "trust" \ "details" \ "residentialStatus" \ "nonUK" \ "sch5atcgga92").get.as[Boolean] mustBe true //TODO: Mapping property sch5atcgga92 missing
      }

      "we have the rest of non uk resident properties" in {
        val domainTrust = trustWithEmploymentTrust.copy(isTrustUkResident = false)
        val json: JsValue = Json.toJson(domainTrust)(trustWrites)

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
    }
  }
}
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

package uk.gov.hmrc.trustapi.rest.resources.core

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._
import uk.gov.hmrc.common.rest.resources.core.Individual.nameWritesToDes
import uk.gov.hmrc.common.rest.resources.core._
import uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries.IndividualBeneficiary
import uk.gov.hmrc.trustapi.rest.resources.core.trusttypes.{EmploymentTrust, TrustType}

case class Trust(name: String,
                 correspondenceAddress: Address,
                 telephoneNumber: String,
                 currentYear: String,
                 commencementDate: DateTime,
                 yearsOfTaxConsequence: Option[YearsOfTaxConsequence] = None,
                 legality: Legality,
                 isTrustUkResident: Boolean,
                 leadTrustee: LeadTrustee,
                 trustees: Trustees,
                 protectors: Protectors,
                 settlors: Settlors,
                 naturalPeople: Option[NaturalPeople] = None,
                 trustType: TrustType,
                 declaration: Declaration,
                 isSchedule5A: Option[Boolean] = None,
                 nonResidentType: Option[String] = None,
                 utr: Option[String] = None
                )


object Trust {
  implicit val dateReads: Reads[DateTime] = Reads.of[String] map (new DateTime(_))
  implicit val dateWrites: Writes[DateTime] = Writes { (dt: DateTime) => JsString(dt.toString("yyyy-MM-dd")) }
  implicit val formats = Json.format[Trust]





  def trustDetailsToDesWrites(isUkResident: Boolean): Writes[Trust] = (
    (JsPath \ "startDate").write[DateTime] and
      (JsPath \ "lawCountry").write[String] and
      (JsPath \ "administrationCountry").writeNullable[String] and
      (JsPath \ "typeOfTrust").write[String] and
      (JsPath \ "deedOfVariation").writeNullable[String] and
      (JsPath \ "interVivos").write[Boolean]  and
      (JsPath \ "efrbsStartDate").writeNullable[DateTime] and
      (JsPath).write[Legality](Legality.residentDetailsToDes(isUkResident))
    )(trustDetails =>  (
    trustDetails.commencementDate,
    trustDetails.legality.governingCountryCode,
    trustDetails.legality.administrationCountryCode,
    trustDetails.trustType.currentTrustType,
    trustDetails.trustType.deedOfVariation,
    trustDetails.trustType.isInterVivo,
    trustDetails.trustType.employmentTrust.flatMap(c=>c.employerFinancedRetirementBenefitSchemeStartDate),
    trustDetails.legality
  ))


  val trustWrites = new Writes[Trust] {
    def writes(trust: Trust) = {
      val trustsMap =  Map("correspondence" -> Json.obj(
        "abroadIndicator" -> JsBoolean(trust.correspondenceAddress.countryCode != "GB"),
        "name" -> JsString(trust.name),
        "phoneNumber" -> JsString(trust.telephoneNumber),
        "address" -> Json.toJson(trust.correspondenceAddress)(Address.writesToDes)),
        "declaration" -> Json.toJson(trust.declaration)(Declaration.writesToDes),
        "details" -> Json.obj(
          "trust"-> Json.obj(
            "details"-> Json.toJson(trust)(Trust.trustDetailsToDesWrites(trust.isTrustUkResident)),
            "entities" -> Json.obj(
              "beneficiary" -> addBeneficiary(trust.trustType)))))
      val naturalPeople =   Map("entities" -> NaturalPeople.addDesNaturalPeople(trust.naturalPeople))

      JsObject(trustsMap ++ naturalPeople ++
          trust.utr.map(v => ("admin", Json.obj("utr" -> JsString(v)))) ++
          trust.yearsOfTaxConsequence.map(v => ("yearsReturns",Json.toJson(v))))
    }
  }



  def addBeneficiary(trustType: TrustType): JsValue ={
    trustType.definedTrusts.head match {
      case  empTrust: EmploymentTrust =>{
       val indvBeneficiary = empTrust.beneficiaries.individualBeneficiaries
          addIndvBenificiary(indvBeneficiary)
      }
      case _ => JsNull
    }
  }



  val writesToBeneficiary: Writes[Individual] = (
    (JsPath \ "name").write[(String,Option[String],String)](nameWritesToDes) and
      (JsPath \ "something").writeNullable[String]
    ) (indv => ((indv.givenName, indv.otherName, indv.familyName),None))

  def addIndvBenificiary(indvBeneficiary: Option[List[IndividualBeneficiary]]): JsValue = {
    if (indvBeneficiary.isEmpty) JsNull else JsObject(Map("individualDetails" -> JsArray(
      indvBeneficiary.get.map(c => Json.toJson(c.individual)(writesToBeneficiary))
    )))
  }




}

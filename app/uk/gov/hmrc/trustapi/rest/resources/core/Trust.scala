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

import play.api.libs.functional.syntax._
import org.joda.time.DateTime
import play.api.libs.json._
import uk.gov.hmrc.common.rest.resources.core._
import uk.gov.hmrc.trustapi.rest.resources.core.trusttypes.TrustType

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

  val ukResidentDetails : Writes[Legality] = (
    (JsPath \ "residentialStatus" \ "uk" \ "scottishLaw").write[Boolean] and
      (JsPath \ "residentialStatus" \ "uk" \ "preOffShore").writeNullable[String]
    )(legality =>(legality.isEstablishedUnderScottishLaw,
    legality.previousOffshoreCountryCode))

  val nonUkResidentDetails : Writes[Legality] = (
    (JsPath \ "residentialStatus" \ "nonUK" \ "sch5atcgga92").write[Boolean] and
      (JsPath \ "residentialStatus" \ "nonUK" \ "s218ihta84").writeNullable[Boolean] and
      (JsPath \ "residentialStatus" \ "nonUK" \ "agentS218IHTA84").writeNullable[Boolean] and
      (JsPath \ "residentialStatus" \ "nonUK" \ "trusteeStatus").writeNullable[String]
    )(_ => (
    true, //TODO: Mapping property sch5atcgga92 missing
    Some(true), //TODO: Mapping property s218ihta84 missing
    Some(true), //TODO: Mapping property agentS218IHTA84 missing
    Some("Non Resident Domiciled"))) //TODO: Mapping property trusteeStatus missing))

  def trustDetailsToDesWrites(isUkResident: Boolean): Writes[Trust] = (
    (JsPath \ "startDate").write[DateTime] and
      (JsPath \ "lawCountry").write[String] and
      (JsPath \ "administrationCountry").writeNullable[String] and
      (JsPath \ "typeOfTrust").write[String] and
      (JsPath \ "deedOfVariation").writeNullable[String] and
      (JsPath \ "interVivos").write[Boolean]  and
      (JsPath \ "efrbsStartDate").writeNullable[DateTime] and
      (JsPath).write[Legality](if (isUkResident) ukResidentDetails else nonUkResidentDetails)
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


  val naturalPeopleWrites : Writes[NaturalPeople] = (
    (JsPath).writeNullable[List[Individual]](individualListWrites) and
      (JsPath \ "somethingto").writeNullable[String]//TODO to remove this
    )(naturalPeople => (naturalPeople.individuals, None))


   val individualWrites: Writes[Individual] = (
     (JsPath \ "name" \ "firstName").write[String] and
      (JsPath \ "name" \ "middleName").writeNullable[String] and
      (JsPath \ "name" \ "lastName").write[String] and
       (JsPath \ "dateOfBirth").write[DateTime]  and
       (JsPath \ "identification" \ "nino").writeNullable[String]
     ) (indv => (indv.givenName, indv.otherName, indv.familyName,indv.dateOfBirth, indv.nino))


  def individualListWrites: Writes[List[Individual]] = (
    (JsPath \"naturalPerson").format[JsArray].inmap(
      (v: JsArray) => v.value.map(v => v.as[Individual]).toList,
      (l: List[Individual]) => JsArray(l.map(indv => Json.toJson(indv)(individualWrites)))
    ))



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
              "details"-> Json.toJson(trust)(Trust.trustDetailsToDesWrites(trust.isTrustUkResident))))
          ) ++
          trust.utr.map(v => ("admin", Json.obj("utr" -> JsString(v)))) ++
          trust.yearsOfTaxConsequence.map(v => ("yearsReturns",Json.toJson(v))) ++
          trust.naturalPeople.map(n => ("entities", Json.toJson(n)(naturalPeopleWrites))
            ))


    }
  }
}

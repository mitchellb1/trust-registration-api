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

package uk.gov.hmrc.models

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Writes}

case class Legality(governingCountryCode: String,
                    administrationCountryCode: Option[String] = None,
                    isEstablishedUnderScottishLaw: Boolean,
                    previousOffshoreCountryCode: Option[String] = None)

object Legality {
  implicit val formats = Json.format[Legality]


  def residentDetailsToDes(isUkResident: Boolean): Writes[Legality] = {
    if (isUkResident)  ukResidentDetailsWritesToDes else nonUkResidentDetailsWritesToDes
  }

  val ukResidentDetailsWritesToDes : Writes[Legality] = (
    (JsPath \ "residentialStatus" \ "uk" \ "scottishLaw").write[Boolean] and
      (JsPath \ "residentialStatus" \ "uk" \ "preOffShore").writeNullable[String]
    )(legality =>(legality.isEstablishedUnderScottishLaw,
    legality.previousOffshoreCountryCode))

  val nonUkResidentDetailsWritesToDes : Writes[Legality] = (
    (JsPath \ "residentialStatus" \ "nonUK" \ "sch5atcgga92").write[Boolean] and
      (JsPath \ "residentialStatus" \ "nonUK" \ "s218ihta84").writeNullable[Boolean] and
      (JsPath \ "residentialStatus" \ "nonUK" \ "agentS218IHTA84").writeNullable[Boolean] and
      (JsPath \ "residentialStatus" \ "nonUK" \ "trusteeStatus").writeNullable[String]
    )(_ => (
    true, //TODO: Mapping property sch5atcgga92 missing
    Some(true), //TODO: Mapping property s218ihta84 missing
    Some(true), //TODO: Mapping property agentS218IHTA84 missing
    Some("Non Resident Domiciled"))) //TODO: Mapping property trusteeStatus missing))
}

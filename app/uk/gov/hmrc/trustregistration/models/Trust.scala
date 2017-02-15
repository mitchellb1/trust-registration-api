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

import org.joda.time.DateTime
import play.api.libs.json.{JsString, Json, Reads, Writes}
import uk.gov.hmrc.trustregistration.models.trusttypes.TrustType

case class Trust(name: String,
                 correspondenceAddress: Address,
                 telephoneNumber: String,
                 currentYear: String,
                 commencementDate: DateTime,
                 yearsOfTaxConsequence: Option[List[Int]] = None,
                 legality: Legality,
                 isTrustUkResident: Boolean,
                 leadTrustee: LeadTrustee,
                 trustees: Trustees,
                 protectors: Protectors,
                 settlors: Settlors,
                 naturalPeople: Option[NaturalPeople] = None,
                 trustType: TrustType,
                 declaration: Option[Declaration] = None,
                 isSchedule5A: Option[Boolean] = None,
                 nonResidentType: Option[String] = None
                )


object Trust {
  implicit val dateReads: Reads[DateTime] = Reads.of[String] map (new DateTime(_))
  implicit val dateWrites: Writes[DateTime] = Writes { (dt: DateTime) => JsString(dt.toString("yyyy-MM-dd")) }
  implicit val formats = Json.format[Trust]
}

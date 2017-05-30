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

package uk.gov.hmrc.common.des

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{JsPath, Writes, _}


//trait DesLeadTrustee
case class DesLeadTrustee(leadTrusteeOrg: Option[DesLeadTrusteeOrg] = None, leadTrusteeInd: Option[DesLeadTrusteeInd] = None)
{
   // val leadTrusteeOrg: Option[DesLeadTrusteeOrg] = None
    //val leadTrusteeInd: Option[DesLeadTrusteeInd] = None
}

object DesLeadTrustee {

 // implicit val dateReads: Reads[DateTime] = Reads.of[String] map (new DateTime(_))
  //implicit val dateWrites: Writes[DateTime] = Writes { (dt: DateTime) => JsString(dt.toString("yyyy-MM-dd")) }

  implicit val desLeadTrusteeWrites = new Writes[DesLeadTrustee] {
    def writes(leadTrustee: DesLeadTrustee) = {
      leadTrustee match {
        case (Some(org: DesLeadTrusteeOrg), _) => Json.obj(
          "name" -> org.name,
          "phoneNumber" -> org.phoneNumber,
          "email" -> org.email,
          "identification" -> org.identification)
        case (_, Some(ind: DesLeadTrusteeInd)) => Json.obj(
          "name" -> ind.name,
          "dateOfBirth" -> ind.dateOfBirth,
          "identification" -> ind.identification,
          "phoneNumber" -> ind.phoneNumber,
          "email" -> ind.email
        )
      }
    }
  }

  implicit val formatsDesLeadTrusteeInd: OFormat[DesLeadTrusteeInd] = Json.format[DesLeadTrusteeInd]
  implicit val formatsDesLeadTrusteeOrg: OFormat[DesLeadTrusteeOrg] = Json.format[DesLeadTrusteeOrg]

  implicit val desLeadTrusteeReads: Unit = {
    def Reads(leadTrustee: DesLeadTrustee): Either[OFormat[DesLeadTrusteeOrg], OFormat[DesLeadTrusteeInd]] = {
      leadTrustee match {
        case (Some(org: DesLeadTrusteeOrg), _) => {
          Left(formatsDesLeadTrusteeOrg)
        }

        case (_, Some(ind: DesLeadTrusteeInd)) => {
          Right(formatsDesLeadTrusteeInd)
        }
      }
    }
  }

  implicit val leadTrusteeOrigFormats: Format[DesLeadTrustee] = Format(desLeadTrusteeReads, desLeadTrusteeWrites)
}
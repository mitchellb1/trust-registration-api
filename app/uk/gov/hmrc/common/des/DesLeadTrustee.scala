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

case class DesLeadTrusteeOrg(name: String, phoneNumber: String, email: Option[String] = None, identification: DesOrgIdentification) extends DesLeadTrustee
case class DesLeadTrusteeInd(name: DesName, dateOfBirth: DateTime, identification: DesIdentification, phoneNumber: String, email: Option[String] = None) extends DesLeadTrustee

trait DesLeadTrustee

object DesLeadTrustee {

  implicit val desLeadTrusteeOrgWrites: Writes[DesLeadTrusteeOrg] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "phoneNumber").write[String] and
      (JsPath \ "email").writeNullable[String] and
      (JsPath \ "identification").write[DesOrgIdentification]
    ){req: DesLeadTrusteeOrg => (req.name, req.phoneNumber, req.email, req.identification)}

  implicit val desLeadTrusteeIndWrites: Writes[DesLeadTrusteeInd] = (
      (JsPath \ "name").write[DesName] and
      (JsPath \ "dateOfBirth").write[String].contramap[DateTime](d => d.toString("yyyy-MM-dd")) and
      (JsPath \ "identification").write[DesIdentification] and
      (JsPath \ "phoneNumber").write[String] and
      (JsPath \ "email").writeNullable[String]
    ){req: DesLeadTrusteeInd => (req.name, req.dateOfBirth, req.identification, req.phoneNumber, req.email)}

  implicit val desLeadTrusteeWrites: Writes[DesLeadTrustee] = Writes[DesLeadTrustee] {
    case r: DesLeadTrusteeOrg => desLeadTrusteeOrgWrites.writes(r)
    case r: DesLeadTrusteeInd => desLeadTrusteeIndWrites.writes(r)
  }

  implicit val desLeadTrusteeOrgReads: Reads[DesLeadTrusteeOrg] = (
      (JsPath \ "name").read[String] and
      (JsPath \ "phoneNumber").read[String] and
      (JsPath \ "email").readNullable[String] and
      (JsPath \ "identification").read[DesOrgIdentification]
    ) (DesLeadTrusteeOrg.apply _)

  implicit val desLeadTrusteeIndReads: Reads[DesLeadTrusteeInd] = (
      (JsPath \ "name").read[DesName] and
      (JsPath \ "dateOfBirth").read[DateTime] and
      (JsPath \ "identification").read[DesIdentification] and
      (JsPath \ "phoneNumber").read[String] and
      (JsPath \ "email").readNullable[String]
    ) (DesLeadTrusteeInd.apply _)

  implicit val desLeadTrusteeReads: Reads[DesLeadTrustee] = Reads[DesLeadTrustee] { json =>
    (json \ "dateOfBirth").validate[DateTime].flatMap {
      case x:DateTime => desLeadTrusteeIndReads.reads(json)
      case _ => desLeadTrusteeOrgReads.reads(json)
    }
  }

  implicit val desLeadTrusteeFormats: Format[DesLeadTrustee] = Format(desLeadTrusteeReads, desLeadTrusteeWrites)

}
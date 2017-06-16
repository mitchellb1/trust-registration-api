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

package uk.gov.hmrc.common.rest.resources.core

import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._


case class Individual(givenName: String,
                      familyName: String,
                      dateOfBirth: DateTime,
                      otherName: Option[String] = None,
                      nino: Option[String] = None,
                      telephoneNumber: Option[String] = None,
                      passportOrIdCard: Option[Passport] = None,
                      correspondenceAddress: Option[Address] = None)

object Individual {
  implicit val dateReads: Reads[DateTime] = Reads.of[String] map (new DateTime(_))
  implicit val dateWrites: Writes[DateTime] = Writes { (dt: DateTime) => JsString(dt.toString("yyyy-MM-dd")) }
  implicit val formats = Json.format[Individual]

  val writesToDes: Writes[Individual] = (
    (JsPath \ "name" \ "firstName").write[String] and
      (JsPath \ "name" \ "middleName").writeNullable[String] and
      (JsPath \ "name" \ "lastName").write[String] and
      (JsPath \ "dateOfBirth").write[DateTime] and
      (JsPath \ "identification" \ "nino").writeNullable[String]
    ) (indv => (indv.givenName, indv.otherName, indv.familyName, indv.dateOfBirth, indv.nino))
}

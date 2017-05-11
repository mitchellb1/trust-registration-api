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

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads}
case class DesPartnership(utr: Option[String] = None, partnershipType: String, partnershipStart: Option[String] = None)

object DesPartnership {
  implicit val formats = Json.format[DesPartnership]

  implicit val reads: Reads[DesPartnership] = (
    (JsPath \ "utr").readNullable[String] and
      (JsPath \ "type").read[String] and
      (JsPath \ "partnershipStart").readNullable[String]
    ) (DesPartnership.apply _)
}
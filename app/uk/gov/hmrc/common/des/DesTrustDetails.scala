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
import play.api.libs.json.{JsPath, Json, Reads}
case class DesTrustDetails(startDate: String, lawCountry: String, administrationCountry: Option[String] = None, residentialStatus: Option[DesResidentialStatus] = None, typeOfTrust: String, deedOfVariation: Option[String] = None, interVivos: Option[Boolean] = None, efrbsStartDate: Option[String] = None)

object DesTrustDetails {
  implicit val formats = Json.format[DesTrustDetails]
}
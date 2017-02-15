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

package uk.gov.hmrc.trustregistration.models.trusttypes

import play.api.libs.json.Json

case class TrustType(willIntestacyTrust: Option[WillIntestacyTrust] = None,
                     interVivoTrust: Option[InterVivoTrust] = None,
                     heritageMaintenanceFundTrust: Option[HeritageMaintenanceFundTrust] = None,
                     flatManagementSinkingFundTrust: Option[FlatManagementSinkingFundTrust] = None,
                     employmentTrust: Option[EmploymentTrust] = None
                    ) {
  val numberOfSubmittedTrustTypes = List(willIntestacyTrust.isDefined,
    interVivoTrust.isDefined,
    heritageMaintenanceFundTrust.isDefined,
    flatManagementSinkingFundTrust.isDefined,
    employmentTrust.isDefined).filter(i => i).size

  require(numberOfSubmittedTrustTypes > 0, "Must have a Trust type")
  require(numberOfSubmittedTrustTypes == 1, "Must have only one Trust type")
}

object TrustType {
  implicit val formats = Json.format[TrustType]
}

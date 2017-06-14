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

package uk.gov.hmrc.trustapi.rest.resources.core.trusttypes

import play.api.libs.json.Json
import uk.gov.hmrc.trustapi.rest.resources.core.{NoTrustTypeException, OnlyOneTrustTypeAllowedException}

case class TrustType(willIntestacyTrust: Option[WillIntestacyTrust] = None,
                     interVivoTrust: Option[InterVivoTrust] = None,
                     heritageMaintenanceFundTrust: Option[HeritageMaintenanceFundTrust] = None,
                     flatManagementSinkingFundTrust: Option[FlatManagementSinkingFundTrust] = None,
                     employmentTrust: Option[EmploymentTrust] = None
                    ) {
  val definedTrusts = List(willIntestacyTrust,
    interVivoTrust,
    heritageMaintenanceFundTrust,
    flatManagementSinkingFundTrust,
    employmentTrust).flatten

  val numberOfSubmittedTrustTypes = definedTrusts.size

  require(numberOfSubmittedTrustTypes > 0, NoTrustTypeException())
  require(numberOfSubmittedTrustTypes == 1, OnlyOneTrustTypeAllowedException())

  val currentTrustType = definedTrusts.head match {
    case _: EmploymentTrust => "Employment Related"
    case _: FlatManagementSinkingFundTrust => "Flat Management Company or Sinking Fund"
    case _: HeritageMaintenanceFundTrust => "Heritage Maintenance Fund"
    case iv : InterVivoTrust => if (iv.dovType.isEmpty)  "Inter vivos Settlement" else "Deed of Variation Trust or Family Arrangement"
    case wi: WillIntestacyTrust =>  if (wi.isDovTypeAddition) "Deed of Variation Trust or Family Arrangement" else "Will Trust or Intestacy Trust"
  }

  val deedOfVariation: Option[String] = definedTrusts.head match {
    case iv : InterVivoTrust => {
      iv.dovType match {
        case Some("dovTypeAbsolute") => Some("Previously there was only an absolute interest under the will")
        case Some("dovTypeReplace") =>  Some("Replaced the will trust")
        case _ => None
      }
    }
    case wi: WillIntestacyTrust => if (wi.isDovTypeAddition) Some("Addition to the will trust") else None
    case _ => None
  }
}

object TrustType {
  implicit val formats = Json.format[TrustType]
}

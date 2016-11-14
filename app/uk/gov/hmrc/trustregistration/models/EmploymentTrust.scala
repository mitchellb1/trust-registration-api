/*
 * Copyright 2016 HM Revenue & Customs
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
import play.api.libs.json.{Json, Reads}


case class EmploymentTrust(assets: Assets,
                           settlors: Settlors,
                           beneficiaries: Beneficiaries,
                           isEmployerFinancedRetirementBenefitScheme: Boolean,
                           employerFinancedRetirementBenefitSchemeStartDate: Option[DateTime] = None) {

  private val atleastOneTypeOfAsset: Boolean = ((assets.monetaryAssets.isDefined && assets.monetaryAssets.get.size > 0) ||
    (assets.propertyAssets.isDefined && assets.propertyAssets.get.size > 0) ||
    (assets.shareAssets.isDefined && assets.shareAssets.get.size > 0) ||
    (assets.businessAssets.isDefined && assets.businessAssets.get.size > 0))

  private val atleastOneRequiredBenefciary: Boolean = (beneficiaries.individualBeneficiaries.isDefined ||
    beneficiaries.directorBeneficiaries.isDefined ||
    beneficiaries.employeeBeneficiaries.isDefined ||
    beneficiaries.otherBeneficiaries.isDefined)

  require(atleastOneTypeOfAsset, "Must have at least one type of Asset")
  require(atleastOneRequiredBenefciary, "Must have at least one required Beneficiary")
}

object EmploymentTrust{
  implicit val dateReads: Reads[DateTime] = Reads.of[String] map (new DateTime(_))
  implicit val assetsFormats = Json.format[EmploymentTrust]
}

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
import uk.gov.hmrc.trustregistration.models.assets.Assets
import uk.gov.hmrc.trustregistration.models.beneficiaries.Beneficiaries

case class InterVivoTrust(assets: Assets,
                          beneficiaries: Beneficiaries,
                          isHoldOverClaim: Boolean,
                          dovType: Option[String] = None) {

  private val atleastOneTypeOfRequiredAsset: Boolean = (assets.monetaryAssets.isDefined && assets.monetaryAssets.get.size > 0) ||
    (assets.propertyAssets.isDefined && assets.propertyAssets.get.size > 0) ||
    (assets.shareAssets.isDefined && assets.shareAssets.get.size > 0) ||
    (assets.businessAssets.isDefined && assets.businessAssets.get.size > 0) ||
    (assets.otherAssets.isDefined && assets.otherAssets.get.size > 0) ||
    (assets.partnershipAssets.isDefined && assets.partnershipAssets.get.size > 0)
  require(atleastOneTypeOfRequiredAsset, "Must have at least one type of required Asset")

  //  private val noOtherTypesOfAsset: Boolean = ( )
  //  require(!noOtherTypesOfAsset, "Must have no other types of Asset")


  private val atleastOneTypeOfRequiredBeneficiaries: Boolean = (beneficiaries.individualBeneficiaries.isDefined && beneficiaries.individualBeneficiaries.get.size > 0) ||
    (beneficiaries.charityBeneficiaries.isDefined && beneficiaries.charityBeneficiaries.get.size > 0) ||
    (beneficiaries.otherBeneficiaries.isDefined && beneficiaries.otherBeneficiaries.get.size > 0) ||
    (beneficiaries.trustBeneficiaries.isDefined && beneficiaries.trustBeneficiaries.get.size > 0) ||
    (beneficiaries.unidentifiedBeneficiaries.isDefined && beneficiaries.unidentifiedBeneficiaries.get.size > 0) ||
    (beneficiaries.companyBeneficiaries.isDefined && beneficiaries.companyBeneficiaries.get.size > 0)
  require(atleastOneTypeOfRequiredBeneficiaries, "Must have at least one type of required Beneficiary")

  private val noOtherTypesOfBeneficiaries: Boolean = (beneficiaries.employeeBeneficiaries.isDefined ||
    beneficiaries.directorBeneficiaries.isDefined)
  require(!noOtherTypesOfBeneficiaries, "Must have no other types of Beneficiary")
}


object InterVivoTrust {
  implicit val formats = Json.format[InterVivoTrust]
}

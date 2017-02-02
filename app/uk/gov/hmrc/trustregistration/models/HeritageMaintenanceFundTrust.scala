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

import play.api.libs.json.Json

case class HeritageMaintenanceFundTrust(assets: Assets,
                                        beneficiaries: Beneficiaries,
                                        isMultiPurposeIncome: Boolean,
                                        deceased: Option[Individual] = None) {

  private val atleastOneTypeOfAsset: Boolean = ((assets.monetaryAssets.isDefined && assets.monetaryAssets.get.size > 0)||
    (assets.propertyAssets.isDefined && assets.propertyAssets.get.size > 0)||
    (assets.shareAssets.isDefined && assets.shareAssets.get.size > 0) ||
    (assets.otherAssets.isDefined && assets.otherAssets.get.size > 0))

  private val atleastOneRequiredBeneficiary: Boolean = beneficiaries.otherBeneficiaries.isDefined

  private val incorrectAsset: Boolean = !assets.partnershipAssets.isDefined && !assets.businessAssets.isDefined

  require(incorrectAsset, "Must not allow this type of asset")
  require(atleastOneTypeOfAsset, "Must have at least one type of Asset")
  require(atleastOneRequiredBeneficiary, "Must have at least one required Beneficiary")
}

object HeritageMaintenanceFundTrust{
  implicit val formats = Json.format[HeritageMaintenanceFundTrust]
}

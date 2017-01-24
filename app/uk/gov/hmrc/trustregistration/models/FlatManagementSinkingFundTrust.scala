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


case class FlatManagementSinkingFundTrust(assets: Assets , beneficiaries: Beneficiaries) {
  private val onlyMonetaryAsset = !assets.otherAssets.isDefined && !assets.shareAssets.isDefined && !assets.propertyAssets.isDefined && !assets.partnershipAssets.isDefined && !assets.businessAssets.isDefined
  private val atleastOneMonetaryAsset: Boolean = (assets.monetaryAssets.isDefined && assets.monetaryAssets.get.size > 0)
  private val onlyOtherBeneficiary = !beneficiaries.individualBeneficiaries.isDefined && !beneficiaries.employeeBeneficiaries.isDefined && !beneficiaries.directorBeneficiaries.isDefined && !beneficiaries.charityBeneficiaries.isDefined

  require(onlyMonetaryAsset, "Only monetary assets are allowed")
  require(atleastOneMonetaryAsset, "Must have at least one monetary asset")
  require(onlyOtherBeneficiary, "Only other beneficiaries are allowed")
}

object FlatManagementSinkingFundTrust{
  implicit val formats = Json.format[FlatManagementSinkingFundTrust]
}

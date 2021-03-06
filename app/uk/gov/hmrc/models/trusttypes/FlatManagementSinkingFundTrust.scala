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

package uk.gov.hmrc.models.trusttypes

import play.api.libs.json.Json
import uk.gov.hmrc.models._
import uk.gov.hmrc.models.assets.Assets
import uk.gov.hmrc.models.beneficiaries.Beneficiaries


case class FlatManagementSinkingFundTrust(assets: Assets, beneficiaries: Beneficiaries) extends BaseTrust{

  private val atleastOneTypeOfRequiredAsset: Boolean = (assets.monetaryAssets.isDefined && assets.monetaryAssets.get.size > 0)
  require(atleastOneTypeOfRequiredAsset, NoAssetsException())

  private val noOtherTypesOfAsset: Boolean = (assets.partnershipAssets.isDefined ||
    assets.businessAssets.isDefined ||
    assets.propertyAssets.isDefined ||
    assets.shareAssets.isDefined ||
    assets.otherAssets.isDefined)
  require(!noOtherTypesOfAsset, NoOtherTypeOfAssetsException())


  private val atleastOneTypeOfRequiredBeneficiaries: Boolean = (
    (beneficiaries.otherBeneficiaries.isDefined && beneficiaries.otherBeneficiaries.get.size > 0))
  require(atleastOneTypeOfRequiredBeneficiaries, NoBeneficiariesException())

  private val noOtherTypesOfBeneficiaries: Boolean = ((beneficiaries.employeeBeneficiaries.isDefined) ||
    (beneficiaries.directorBeneficiaries.isDefined) ||
    (beneficiaries.trustBeneficiaries.isDefined) ||
    (beneficiaries.unidentifiedBeneficiaries.isDefined) ||
    (beneficiaries.companyBeneficiaries.isDefined) ||
    (beneficiaries.individualBeneficiaries.isDefined) ||
    (beneficiaries.charityBeneficiaries.isDefined) ||
    (beneficiaries.largeNumbersCompanyBeneficiaries.isDefined))
  require(!noOtherTypesOfBeneficiaries, NoOtherTypeOfBeneficiariesException())
}

object FlatManagementSinkingFundTrust {
  implicit val formats = Json.format[FlatManagementSinkingFundTrust]
}

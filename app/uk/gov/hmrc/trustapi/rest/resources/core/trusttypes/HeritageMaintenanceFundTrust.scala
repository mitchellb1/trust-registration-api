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

import play.api.libs.json.{JsArray, JsValue, Json}
import uk.gov.hmrc.common.rest.resources.core.Individual
import uk.gov.hmrc.trustapi.rest.resources.core.assets.Assets
import uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries.{Beneficiaries, CompanyBeneficiary, IndividualBeneficiary}
import uk.gov.hmrc.trustapi.rest.resources.core.{NoAssetsException, NoBeneficiariesException, NoOtherTypeOfAssetsException, NoOtherTypeOfBeneficiariesException}

case class HeritageMaintenanceFundTrust(assets: Assets,
                                        beneficiaries: Beneficiaries,
                                        isMultiPurposeIncome: Boolean,
                                        deceased: Option[Individual] = None) extends BaseTrust{

  private val atleastOneTypeOfRequiredAsset: Boolean = ((assets.monetaryAssets.isDefined && assets.monetaryAssets.get.size > 0) ||
    (assets.propertyAssets.isDefined && assets.propertyAssets.get.size > 0) ||
    (assets.shareAssets.isDefined && assets.shareAssets.get.size > 0) ||

    (assets.otherAssets.isDefined && assets.otherAssets.get.size > 0))
  require(atleastOneTypeOfRequiredAsset, NoAssetsException())

  private val noOtherTypesOfAsset: Boolean = (assets.partnershipAssets.isDefined || assets.businessAssets.isDefined)
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

  override def addIndividualBeneficiary(): Option[JsValue] = {
    beneficiaries.individualBeneficiaries.map(b => JsArray(b.map(c => Json.toJson(c)(IndividualBeneficiary.writesToDes))))
  }

  override def addCompanyBeneficiaries(): Option[JsValue] = {
    beneficiaries.companyBeneficiaries.map(b => JsArray(b.map(c => Json.toJson(c)(CompanyBeneficiary.writesToDes))))
  }
}

object HeritageMaintenanceFundTrust {
  implicit val formats = Json.format[HeritageMaintenanceFundTrust]
}

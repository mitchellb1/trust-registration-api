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

import org.joda.time.DateTime
import play.api.libs.json.{JsArray, JsValue, Json, Reads}
import uk.gov.hmrc.trustapi.rest.resources.core.assets.Assets
import uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries.{Beneficiaries, CompanyBeneficiary, IndividualBeneficiary}
import uk.gov.hmrc.trustapi.rest.resources.core.{NoAssetsException, NoBeneficiariesException, NoOtherTypeOfAssetsException, NoOtherTypeOfBeneficiariesException}

trait BaseTrust {
  val beneficiaries: Beneficiaries

  def addIndividualBeneficiary(): Option[JsValue] = {
    beneficiaries.individualBeneficiaries.map(b => JsArray(b.map(c => Json.toJson(c)(IndividualBeneficiary.writesToDes))))
  }

  def addCompanyBeneficiaries(): Option[JsValue] = {
    beneficiaries.companyBeneficiaries.map(b => JsArray(b.map(c => Json.toJson(c)(CompanyBeneficiary.writesToDes))))
  }
}

case class EmploymentTrust(assets: Assets,
                           beneficiaries: Beneficiaries,
                           isEmployerFinancedRetirementBenefitScheme: Option[Boolean] = None,
                           employerFinancedRetirementBenefitSchemeStartDate: Option[DateTime] = None) extends BaseTrust{

  private val atleastOneTypeOfRequiredAsset: Boolean = ((assets.monetaryAssets.isDefined && assets.monetaryAssets.get.size > 0) ||
    (assets.propertyAssets.isDefined && assets.propertyAssets.get.size > 0) ||
    (assets.shareAssets.isDefined && assets.shareAssets.get.size > 0) ||
    (assets.businessAssets.isDefined && assets.businessAssets.get.size > 0) ||
    (assets.otherAssets.isDefined && assets.otherAssets.get.size > 0))
  require(atleastOneTypeOfRequiredAsset, NoAssetsException())

  private val noOtherTypesOfAsset: Boolean = ((assets.partnershipAssets.isDefined))
  require(!noOtherTypesOfAsset, NoOtherTypeOfAssetsException())


  private val atleastOneTypeOfRequiredBeneficiaries: Boolean = ((beneficiaries.individualBeneficiaries.isDefined && beneficiaries.individualBeneficiaries.get.size > 0) ||
    (beneficiaries.directorBeneficiaries.isDefined && beneficiaries.directorBeneficiaries.get.size > 0) ||
    (beneficiaries.otherBeneficiaries.isDefined && beneficiaries.otherBeneficiaries.get.size > 0) ||
    (beneficiaries.trustBeneficiaries.isDefined && beneficiaries.trustBeneficiaries.get.size > 0) ||
    (beneficiaries.unidentifiedBeneficiaries.isDefined && beneficiaries.unidentifiedBeneficiaries.get.size > 0) ||
    (beneficiaries.employeeBeneficiaries.isDefined && beneficiaries.employeeBeneficiaries.get.size > 0) ||
    (beneficiaries.companyBeneficiaries.isDefined && beneficiaries.companyBeneficiaries.get.size > 0) ||
    (beneficiaries.largeNumbersCompanyBeneficiaries.isDefined && beneficiaries.largeNumbersCompanyBeneficiaries.get.size > 0))
  require(atleastOneTypeOfRequiredBeneficiaries, NoBeneficiariesException())

  private val noOtherTypesOfBeneficiaries: Boolean = beneficiaries.charityBeneficiaries.isDefined
  require(!noOtherTypesOfBeneficiaries, NoOtherTypeOfBeneficiariesException())
}

object EmploymentTrust {
  implicit val dateReads: Reads[DateTime] = Reads.of[String] map (new DateTime(_))
  implicit val assetsFormats = Json.format[EmploymentTrust]
}

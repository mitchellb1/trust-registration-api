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

package uk.gov.hmrc.trustapi.rest.resources.core

import play.api.libs.json.{JsArray, JsValue, Json}
import uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries.{Beneficiaries, CompanyBeneficiary, IndividualBeneficiary, TrustBeneficiary}


trait BaseTrust {
  val beneficiaries: Beneficiaries

  def addIndividualBeneficiary(): Option[JsValue] = {
    beneficiaries.individualBeneficiaries.map(b => JsArray(b.map(c => Json.toJson(c)(IndividualBeneficiary.writesToDes))))
  }

  def addCompanyBeneficiaries(): Option[JsValue] = {
    beneficiaries.companyBeneficiaries.map(b => JsArray(b.map(c => Json.toJson(c)(CompanyBeneficiary.writesToDes))))
  }

  def addTrustBeneficiaries(): Option[JsValue] = {
    beneficiaries.trustBeneficiaries.map(b => JsArray(b.map(c=>Json.toJson(c)(TrustBeneficiary.writesToDes))))
  }
}

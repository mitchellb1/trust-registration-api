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

package uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries

import play.api.libs.functional.syntax._
import play.api.libs.json.Writes._
import play.api.libs.json._
import uk.gov.hmrc.common.rest.resources.core.Address
import uk.gov.hmrc.trustapi.rest.resources.core.trusttypes.TrustType


case class Beneficiaries(individualBeneficiaries: Option[List[IndividualBeneficiary]] = None,
                         employeeBeneficiaries: Option[List[EmployeeBeneficiary]] = None,
                         directorBeneficiaries: Option[List[DirectorBeneficiary]] = None,
                         charityBeneficiaries: Option[List[CharityBeneficiary]] = None,
                         otherBeneficiaries: Option[List[OtherBeneficiary]] = None,
                         trustBeneficiaries: Option[List[TrustBeneficiary]] = None,
                         companyBeneficiaries: Option[List[CompanyBeneficiary]] = None,
                         unidentifiedBeneficiaries: Option[List[UnidentifiedBeneficiary]] = None,
                         largeNumbersCompanyBeneficiaries: Option[List[LargeNumbersCompanyBeneficiaries]] = None)

object Beneficiaries {

  val identificationWritesToDes : Writes[(Address,Option[String])] = (
    (JsPath \ "identification" \ "address").write[Address](Address.writesToDes) and
      (JsPath \ "identification" \ "utr").writeNullable[String]
    )(i=>(i._1,i._2))

  implicit val beneficiariesFormat = Json.format[Beneficiaries]

  val beneficiaryWritesToDes: Writes[TrustType] = (
    (JsPath \ "individualDetails").writeNullable[JsValue] and
      (JsPath \ "company").writeNullable[JsValue] and
      (JsPath \ "trust").writeNullable[JsValue] and
      (JsPath \ "charity").writeNullable[JsValue] and
      (JsPath \ "unidentified").writeNullable[JsValue] and
      (JsPath \ "large").writeNullable[JsValue] and
      (JsPath \ "other").writeNullable[JsValue]
    ) (b => (b.selectedTrust.addIndividualBeneficiary(), b.selectedTrust.addCompanyBeneficiaries(), b.selectedTrust.addTrustBeneficiaries(),
    b.selectedTrust.addCharityBeneficiaries(),
    b.selectedTrust.addUnidentifiedBeneficiaries(),
    b.selectedTrust.addLargeTypeBeneficiaries(),
    b.selectedTrust.addOtherBeneficiaries()))
}




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

case class IndividualBeneficiary(
                                  individual: Individual,
                                  isVulnerable: Boolean)

object IndividualBeneficiary {
  implicit val individualBeneficiaryFormats = Json.format[IndividualBeneficiary]
}

case class EmployeeBeneficiary(individual: Individual)

object EmployeeBeneficiary {
  implicit val employeeBeneficiaryFormats = Json.format[EmployeeBeneficiary]
}

case class DirectorBeneficiary(
                                individual: Individual)

object DirectorBeneficiary {
  implicit val directorBeneficiaryFormats = Json.format[DirectorBeneficiary]
}

case class CharityBeneficiary(charityName: String,
                              charityNumber: String,
                              correspondenceAddress: Address)

object CharityBeneficiary {
  implicit val charityBeneficiaryFormats = Json.format[CharityBeneficiary]
}

case class OtherBeneficiary(
                             beneficiaryDescription: String,
                             correspondenceAddress: Address)

object OtherBeneficiary {
  implicit val otherBeneficiaryFormats = Json.format[OtherBeneficiary]
}

case class TrustBeneficiary(
                             trustBeneficiaryName: String,
                             trustBeneficiaryUTR: Option[String] = None,
                             correspondenceAddress: Address)

case class CompanyBeneficiary(company: Company)

case class Beneficiaries(
  individualBeneficiaries: Option[List[IndividualBeneficiary]] = None,
  employeeBeneficiaries: Option[List[EmployeeBeneficiary]] = None,
  directorBeneficiaries: Option[List[DirectorBeneficiary]] = None,
  charityBeneficiaries: Option[List[CharityBeneficiary]] = None,
  otherBeneficiaries: Option[List[OtherBeneficiary]] = None,
  trustBeneficiaries: Option[List[TrustBeneficiary]] = None,
  companyBeneficiaries: Option[List[CompanyBeneficiary]] = None){

  private val atLeastOneBeneficiary: Boolean =
    (individualBeneficiaries.isDefined && individualBeneficiaries.get.size > 0) ||
    (employeeBeneficiaries.isDefined && employeeBeneficiaries.get.size > 0) ||
    (directorBeneficiaries.isDefined && directorBeneficiaries.get.size > 0) ||
    (charityBeneficiaries.isDefined && charityBeneficiaries.get.size > 0) ||
    (otherBeneficiaries.isDefined && otherBeneficiaries.get.size > 0 ||
    (trustBeneficiaries.isDefined && trustBeneficiaries.get.size > 0)||
    (companyBeneficiaries.isDefined && companyBeneficiaries.get.size > 0))
  require(atLeastOneBeneficiary, "Must have at least one beneficiary")
}

object TrustBeneficiary{
  implicit val trustBeneficiaryFormats = Json.format[TrustBeneficiary]
}

object CompanyBeneficiary {
  implicit val companyBeneficiaryFormats = Json.format[CompanyBeneficiary]
}

object Beneficiaries {
  implicit val beneficiariesFormat = Json.format[Beneficiaries]
}

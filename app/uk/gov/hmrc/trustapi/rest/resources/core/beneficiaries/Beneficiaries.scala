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

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.Writes._
import play.api.libs.json._
import uk.gov.hmrc.common.rest.resources.core.Individual
import uk.gov.hmrc.common.rest.resources.core.Individual.nameWritesToDes
import uk.gov.hmrc.trustapi.rest.resources.core.trusttypes.{EmploymentTrust, InterVivoTrust, TrustType}


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
  implicit val beneficiariesFormat = Json.format[Beneficiaries]


  val beneficiaryWritesToDes: Writes[TrustType] = (
    (JsPath \ "individualDetails").writeNullable[JsValue] and
      (JsPath \ "company").writeNullable[JsValue]
    ) (b => (addIndividualBeneficiary(b), addCompanyBeneficiary(b)))

  val companyBeneficiaryWritesToDes: Writes[CompanyBeneficiary] = (
    (JsPath \ "organisationName").write[String] and
      (JsPath \ "beneficiaryDiscretion").write[Boolean] and
      (JsPath \ "beneficiaryShareOfIncome").writeNullable[String]
    ) (c => (c.company.name, c.incomeDistribution.isIncomeAtTrusteeDiscretion, c.incomeDistribution.shareOfIncome.map(c => c.toString)))

  val individualBeneficiaryWritesToDes: Writes[IndividualBeneficiary] = (
    (JsPath \ "name").write[(String, Option[String], String)](nameWritesToDes) and
      (JsPath \ "dateOfBirth").write[DateTime](jodaDateWrites("YYYY-MM-DD")) and
      (JsPath \ "vulnerableBeneficiary").write[Boolean] and
      (JsPath \ "beneficiaryType").write[String] and
      (JsPath \ "beneficiaryDiscretion").write[Boolean] and
      (JsPath \ "beneficiaryShareOfIncome").writeNullable[String] and
      (JsPath \ "identification").write[Individual](Individual.identificationWritesToDes)
    ) (i => ((i.individual.givenName, i.individual.otherName, i.individual.familyName),
    i.individual.dateOfBirth,
    i.isVulnerable,
    i.beneficiaryType,
    i.incomeDistribution.isIncomeAtTrusteeDiscretion,
    i.incomeDistribution.shareOfIncome.map(c => c.toString),
    i.individual))

  private def addBeneficiaries[T](beneficiaries: Option[List[T]], writes: Writes[T]) ={
    beneficiaries.map(b => JsArray(b.map(c => Json.toJson(c)(writes))))
  }

  private def addIndividualBeneficiary(trustType: TrustType): Option[JsValue] = {
    trustType.definedTrusts.head match {
      case empTrust: EmploymentTrust => {
        addBeneficiaries(empTrust.beneficiaries.individualBeneficiaries,individualBeneficiaryWritesToDes)
      }
      case viviTrust : InterVivoTrust =>
        addBeneficiaries(viviTrust.beneficiaries.individualBeneficiaries, individualBeneficiaryWritesToDes)
      case _ => None
    }
  }

  private def addCompanyBeneficiary(trustType: TrustType): Option[JsValue] = {
    trustType.definedTrusts.head match {
      case empTrust: EmploymentTrust => {
        addBeneficiaries(empTrust.beneficiaries.companyBeneficiaries,companyBeneficiaryWritesToDes)
      }
      case _ => None
    }
  }
}




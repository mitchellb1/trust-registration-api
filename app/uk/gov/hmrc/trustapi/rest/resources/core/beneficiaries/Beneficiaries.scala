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
import play.api.libs.json._
import uk.gov.hmrc.common.rest.resources.core.Individual
import uk.gov.hmrc.common.rest.resources.core.Individual.nameWritesToDes
import uk.gov.hmrc.trustapi.rest.resources.core.trusttypes.{EmploymentTrust, TrustType}
import play.api.libs.functional.syntax._


case class Beneficiaries(individualBeneficiaries: Option[List[IndividualBeneficiary]] = None,
                         employeeBeneficiaries: Option[List[EmployeeBeneficiary]] = None,
                         directorBeneficiaries: Option[List[DirectorBeneficiary]] = None,
                         charityBeneficiaries: Option[List[CharityBeneficiary]] = None,
                         otherBeneficiaries: Option[List[OtherBeneficiary]] = None,
                         trustBeneficiaries: Option[List[TrustBeneficiary]] = None,
                         companyBeneficiaries: Option[List[CompanyBeneficiary]] = None,
                         unidentifiedBeneficiaries: Option[List[UnidentifiedBeneficiary]] = None,
                         largeNumbersCompanyBeneficiaries : Option[List[LargeNumbersCompanyBeneficiaries]] = None)

object Beneficiaries {
  implicit val beneficiariesFormat = Json.format[Beneficiaries]

  def addBeneficiary(trustType: TrustType): JsValue ={
    trustType.definedTrusts.head match {
      case  empTrust: EmploymentTrust =>{
        empTrust.beneficiaries.individualBeneficiaries.map(ind=>JsObject(Map("individualDetails" -> JsArray(ind.map(c => Json.toJson(c.individual)(writesToBeneficiary)))))).getOrElse(JsNull)
      }
      case _ => JsNull
    }
  }

  val writesToBeneficiary: Writes[Individual] = (
    (JsPath \ "name").write[(String,Option[String],String)](nameWritesToDes) and
      (JsPath \ "dateOfBirth").write[DateTime]
    ) (indv => ((indv.givenName, indv.otherName, indv.familyName),indv.dateOfBirth))
}




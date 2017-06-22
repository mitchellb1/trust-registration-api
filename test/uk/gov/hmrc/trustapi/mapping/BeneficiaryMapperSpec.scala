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

package uk.gov.hmrc.trustapi.mapping

import org.joda.time.DateTime
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import uk.gov.hmrc.trustapi.rest.resources.core.Trust
import uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries.{Beneficiaries, IncomeDistribution, IndividualBeneficiary}
import uk.gov.hmrc.trustapi.rest.resources.core.trusttypes.{EmploymentTrust, TrustType}
import uk.gov.hmrc.utils.ScalaDataExamples

class BeneficiaryMapperSpec extends PlaySpec with OneAppPerSuite with ScalaDataExamples {

  "Beneficiary Mapper" should {
    "Map a domain representation of beneficiaries to a valid JSON Representation of DES beneficiaries" when {

      val domainTrust = trustWithEmploymentTrust
      val json = Json.toJson(domainTrust)(Trust.trustWrites)

      "we have individual beneficiaries" when {
        "we have a name" in {
          val beneficiariesList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "individualDetails")(0)

          (beneficiariesList \ "name" \ "firstName").get.as[String] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.individualBeneficiaries.get.head.individual.givenName
        }

       "we have a name in an intervivo trust " in {
          val domainTrust = trustWithInterVivoTrustDOV
          val json = Json.toJson(domainTrust)(Trust.trustWrites)

          val beneficiariesList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "individualDetails")(0)
          (beneficiariesList \ "name" \ "firstName").get.as[String] mustBe domainTrust.trustType.interVivoTrust.get.beneficiaries.individualBeneficiaries.get.head.individual.givenName
        }

        "we have a name with otherName/middelName" in {
          val employmentTrust = Some(EmploymentTrust(assets,Beneficiaries(Some(List(IndividualBeneficiary(individualWithOtherName,false, incomeDistribution)))),Some(true),Some(new DateTime("1900-01-01"))))
          val domainTrust = trustWithEmploymentTrust.copy(trustType = TrustType(employmentTrust=employmentTrust))
          val json = Json.toJson(domainTrust)(Trust.trustWrites)

          val beneficiariesList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "individualDetails")(0)
          (beneficiariesList \ "name" \ "middleName").get.as[String] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.individualBeneficiaries.get.head.individual.otherName.get
        }

        "we have date of birth details" in {
          val beneficiariesList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "individualDetails")(0)

          (beneficiariesList \ "dateOfBirth").get.as[DateTime] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.individualBeneficiaries.get.head.individual.dateOfBirth
          (beneficiariesList \ "dateOfBirth").get.as[String] mustBe "1900-01-01"
        }

        "we have a vulnerable beneficiary flag" in {
          val beneficiariesList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "individualDetails")(0)

          (beneficiariesList \ "vulnerableBeneficiary").get.as[Boolean] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.individualBeneficiaries.get.head.isVulnerable
        }

        "we have beneficiaryType details" in {
          val beneficiariesList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "individualDetails")(0)

          (beneficiariesList \ "beneficiaryType").get.as[String] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.individualBeneficiaries.get.head.beneficiaryType
        }

        "we have beneficiaryDiscretion details " in {
          val beneficiariesList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "individualDetails")(0)

          (beneficiariesList \ "beneficiaryDiscretion").get.as[Boolean] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.individualBeneficiaries.get.head.incomeDistribution.isIncomeAtTrusteeDiscretion
        }

        "we have beneficiaryShareOfIncome details" in {
          val beneficiariesList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "individualDetails")(0)

          (beneficiariesList \ "beneficiaryShareOfIncome").get.as[String] mustBe String.valueOf(domainTrust.trustType.employmentTrust.get.beneficiaries.individualBeneficiaries.get.head.incomeDistribution.shareOfIncome.get)
        }

        "we don't have beneficiaryShafeOfIncome details" in {
          val employmentTrust = Some(EmploymentTrust(assets,Beneficiaries(Some(List(IndividualBeneficiary(individualWithOtherName,false, incomeDistribution.copy(shareOfIncome = None, isIncomeAtTrusteeDiscretion = true))))),Some(true),Some(new DateTime("1900-01-01"))))
          val domainTrust = trustWithEmploymentTrust.copy(trustType = TrustType(employmentTrust=employmentTrust))
          val json = Json.toJson(domainTrust)(Trust.trustWrites)
          val beneficiariesList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "individualDetails")(0)
          (beneficiariesList \ "beneficiaryShareOfIncome").validate[String].isError mustBe true
        }

        "we have beneficiary with identification details with passport details with passport number" in {
          val beneficiariesList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "individualDetails")(0)

          (beneficiariesList \ "identification" \ "passport" \ "number").get.as[String] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.individualBeneficiaries.get.head.individual.passportOrIdCard.get.referenceNumber
        }
      }

      "we have company beneficiaries" when {
        val domainTrust = trustWithEmploymentTrustAndCompanyBen
        val json = Json.toJson(domainTrust)(Trust.trustWrites)

        "we have organisation name " in {
          val companyBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "company")(0)

          (companyBeneficaryList \ "organisationName").get.as[String] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.companyBeneficiaries.get.head.company.name
        }

        "we have beneficiaryDiscretion flag " in {
          val companyBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "company")(0)

          (companyBeneficaryList \ "beneficiaryDiscretion").get.as[Boolean] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.companyBeneficiaries.get.head.incomeDistribution.isIncomeAtTrusteeDiscretion
        }

        "we have beneficiaryShareOfIncome details " in {
          val companyBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "company")(0)

          (companyBeneficaryList \ "beneficiaryShareOfIncome").get.as[String] mustBe  String.valueOf(domainTrust.trustType.employmentTrust.get.beneficiaries.companyBeneficiaries.get.head.incomeDistribution.shareOfIncome.get)
        }

        "we have no beneficiaryShareOfIncome " in {
          val domainTrust = trustWithEmploymentTrustAndCompanyBen.copy(trustType = TrustType(employmentTrust = Some(EmploymentTrust(assets,Beneficiaries(Some(List(IndividualBeneficiary(individual,false, IncomeDistribution(true,None)))),None,None)))))
          val json = Json.toJson(domainTrust)(Trust.trustWrites)
          val companyBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "company")(0)

          (companyBeneficaryList \ "beneficiaryShareOfIncome").validate[String].isError mustBe  true
        }

        "we have identification details having address details" in {
          val companyBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "company")(0)

          (companyBeneficaryList \ "identification" \ "address" \ "line1").get.as[String] mustBe  domainTrust.trustType.employmentTrust.get.beneficiaries.companyBeneficiaries.get.head.company.correspondenceAddress.line1
        }

        "we have identification details having a utr and address" in {
          val companyBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "company")(0)

          (companyBeneficaryList \ "identification" \ "utr").get.as[String] mustBe  domainTrust.trustType.employmentTrust.get.beneficiaries.companyBeneficiaries.get.head.company.referenceNumber.get
          (companyBeneficaryList \ "identification" \ "address" \ "line1").get.as[String] mustBe  domainTrust.trustType.employmentTrust.get.beneficiaries.companyBeneficiaries.get.head.company.correspondenceAddress.line1
        }

        "we have identification details without having utr and we have an address " in {
          val domainTrust = trustWithEmploymentTrustAndCompanyBen.copy(trustType = TrustType(employmentTrust = Some(EmploymentTrust(assets,Beneficiaries(companyBeneficiaries = Some(List(companyBeneficiary.copy(company.copy(referenceNumber = None)))))))))
          val json = Json.toJson(domainTrust)(Trust.trustWrites)
          val companyBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "company")(0)
          (companyBeneficaryList \ "identification" \ "utr").validate[String].isError mustBe true
          (companyBeneficaryList \ "identification" \ "address" \ "line1").get.as[String] mustBe  domainTrust.trustType.employmentTrust.get.beneficiaries.companyBeneficiaries.get.head.company.correspondenceAddress.line1
        }

      }
    }
  }
}

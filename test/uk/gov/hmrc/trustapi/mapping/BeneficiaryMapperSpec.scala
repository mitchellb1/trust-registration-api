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
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import uk.gov.hmrc.trustapi.rest.resources.core.Trust
import uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries._
import uk.gov.hmrc.trustapi.rest.resources.core.trusttypes.{EmploymentTrust, TrustType, WillIntestacyTrust}
import uk.gov.hmrc.utils.ScalaDataExamples

class BeneficiaryMapperSpec extends PlaySpec with ScalaDataExamples {

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

      "we have trust beneficiaries" when {

        val domainTrust = trustWithWillIntestacyTrust.copy(trustType = TrustType(willIntestacyTrust = Some(willIntestacyTrustWithTrustBeneficiary)))
        val json = Json.toJson(domainTrust)(Trust.trustWrites)

        "we have organisationName  details" in {
          val trustBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "trust")(0)

          (trustBeneficaryList \ "organisationName").get.as[String] mustBe domainTrust.trustType.willIntestacyTrust.get.beneficiaries.trustBeneficiaries.get.head.trustBeneficiaryName
        }

        "we have beneficiaryDiscretion flag " in {
          val trustBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "trust")(0)

          (trustBeneficaryList \ "beneficiaryDiscretion").get.as[Boolean] mustBe domainTrust.trustType.willIntestacyTrust.get.beneficiaries.trustBeneficiaries.get.head.incomeDistribution.isIncomeAtTrusteeDiscretion
        }

        "we have beneficiaryShareOfIncome details  " in {
          val trustBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "trust")(0)

          (trustBeneficaryList \ "beneficiaryShareOfIncome").get.as[String] mustBe  String.valueOf(domainTrust.trustType.willIntestacyTrust.get.beneficiaries.trustBeneficiaries.get.head.incomeDistribution.shareOfIncome.get)
        }

        "we have no beneficiaryShareOfIncome " in {
          val trustBeneficiaryWithoutShareOfIncome =  trustBeneficiary.copy(incomeDistribution = incomeDistribution.copy(shareOfIncome = None,isIncomeAtTrusteeDiscretion = true))
          val domainTrust = trustWithWillIntestacyTrust.copy(trustType = TrustType(willIntestacyTrust = Some(WillIntestacyTrust(assets,Beneficiaries(trustBeneficiaries = Some(List(trustBeneficiaryWithoutShareOfIncome))), deceased, false))))
          val json = Json.toJson(domainTrust)(Trust.trustWrites)

          val trustBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "trust")(0)

          (trustBeneficaryList \ "beneficiaryShareOfIncome").validate[String].isError mustBe  true
        }

        "we have identification details having address details " in {
          val trustBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "trust")(0)

          (trustBeneficaryList \ "identification" \ "address" \ "line1").get.as[String] mustBe  domainTrust.trustType.willIntestacyTrust.get.beneficiaries.trustBeneficiaries.get.head.correspondenceAddress.line1
        }

        "we have identification details having a utr and address" in {
          val trustBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "trust")(0)

          (trustBeneficaryList \ "identification" \ "utr").get.as[String] mustBe  domainTrust.trustType.willIntestacyTrust.get.beneficiaries.trustBeneficiaries.get.head.trustBeneficiaryUTR.get
          (trustBeneficaryList \ "identification" \ "address" \ "line1").get.as[String] mustBe  domainTrust.trustType.willIntestacyTrust.get.beneficiaries.trustBeneficiaries.get.head.correspondenceAddress.line1
        }

        "we have identification details without having utr and we have an address" in {
          val domainTrust = trustWithWillIntestacyTrust.copy(trustType = TrustType(willIntestacyTrust = Some(WillIntestacyTrust(assets,Beneficiaries(trustBeneficiaries = Some(List(trustBeneficiary.copy(trustBeneficiaryUTR = None)))), deceased, false))))
          val json = Json.toJson(domainTrust)(Trust.trustWrites)

          val trustBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "trust")(0)

          (trustBeneficaryList \ "identification" \ "utr").validate[String].isError mustBe true
          (trustBeneficaryList \ "identification" \ "address" \ "line1").get.as[String] mustBe  domainTrust.trustType.willIntestacyTrust.get.beneficiaries.trustBeneficiaries.get.head.correspondenceAddress.line1
        }
      }

      "we have charity beneficiaries" when {

        val domainTrust = trustWithWillIntestacyTrust.copy(trustType = TrustType(willIntestacyTrust = Some(willIntestacyTrustWithCharityBeneficiary)))
        val json = Json.toJson(domainTrust)(Trust.trustWrites)

        "we have organisationName  details" in {
          val charityBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "charity") (0)
          (charityBeneficaryList \ "organisationName").get.as[String] mustBe domainTrust.trustType.willIntestacyTrust.get.beneficiaries.charityBeneficiaries.get.head.charityName
        }

        "we have beneficiaryDiscretion flag " in {
          val charityBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "charity")(0)
          (charityBeneficaryList \ "beneficiaryDiscretion").get.as[Boolean] mustBe domainTrust.trustType.willIntestacyTrust.get.beneficiaries.charityBeneficiaries.get.head.incomeDistribution.isIncomeAtTrusteeDiscretion
        }

        "we have beneficiaryShareOfIncome details  " in {
          val charityBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "charity")(0)
          (charityBeneficaryList \ "beneficiaryShareOfIncome").get.as[String] mustBe
            String.valueOf(domainTrust.trustType.willIntestacyTrust.get.beneficiaries.charityBeneficiaries.get.head.incomeDistribution.shareOfIncome.get)
        }

        "we have no beneficiaryShareOfIncome " in {
          val charityBeneficiaryWithoutShareOfIncome =  charityBeneficiary.copy(incomeDistribution = incomeDistribution.copy(shareOfIncome = None,isIncomeAtTrusteeDiscretion = true))
          val domainTrust = trustWithWillIntestacyTrust.copy(trustType = TrustType(
            willIntestacyTrust = Some(WillIntestacyTrust(assets,Beneficiaries(charityBeneficiaries =  Some(List(charityBeneficiaryWithoutShareOfIncome))), deceased, false))))
          val json = Json.toJson(domainTrust)(Trust.trustWrites)

          val charityBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "charity")(0)

          (charityBeneficaryList \ "beneficiaryShareOfIncome").validate[String].isError mustBe  true
        }

        "we have identification details having address details " in {
          val charityBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "charity")(0)

          (charityBeneficaryList \ "identification" \ "address" \ "line1").get.as[String] mustBe  domainTrust.trustType.willIntestacyTrust.get.beneficiaries.charityBeneficiaries.get.head.correspondenceAddress.line1
        }

        "we have identification details having a utr and address" in {
          val charityBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "charity")(0)

          (charityBeneficaryList \ "identification" \ "utr").get.as[String] mustBe  domainTrust.trustType.willIntestacyTrust.get.beneficiaries.charityBeneficiaries.get.head.charityNumber
          (charityBeneficaryList \ "identification" \ "address" \ "line1").get.as[String] mustBe  domainTrust.trustType.willIntestacyTrust.get.beneficiaries.charityBeneficiaries.get.head.correspondenceAddress.line1
        }

        "we have identification details without having utr and we have an address" in {
          val domainTrust = trustWithWillIntestacyTrust.copy(trustType = TrustType(willIntestacyTrust =
            Some(WillIntestacyTrust(assets,Beneficiaries(charityBeneficiaries = Some(List(charityBeneficiary.copy(charityNumber = "")))), deceased, false))))
          val json = Json.toJson(domainTrust)(Trust.trustWrites)

          val charityBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "charity")(0)
          (charityBeneficaryList \ "identification" \ "utr").get.as[String] mustBe ""
          (charityBeneficaryList \ "identification" \ "address" \ "line1").get.as[String] mustBe  domainTrust.trustType.willIntestacyTrust.get.beneficiaries.charityBeneficiaries.get.head.correspondenceAddress.line1
        }
      }

      "we have unidentifiedType beneficiaries" when {
        val empTrustWithUnidentifiedBeneficiary = Some(EmploymentTrust(assets,Beneficiaries(unidentifiedBeneficiaries = Some(List(unidentifiedBeneficiary))),Some(true),Some(new DateTime("1900-01-01"))))

        val domainTrust = trustWithEmploymentTrust.copy(trustType = TrustType(employmentTrust=empTrustWithUnidentifiedBeneficiary))
        val json = Json.toJson(domainTrust)(Trust.trustWrites)

        "we have description  details" in {
          val unidentifiedBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "unidentified") (0)
          (unidentifiedBeneficaryList \ "description").get.as[String] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.unidentifiedBeneficiaries.get.head.description
        }

        "we have beneficiaryDiscretion flag " in {
          val unidentifiedBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "unidentified") (0)
          (unidentifiedBeneficaryList \ "beneficiaryDiscretion").get.as[Boolean] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.unidentifiedBeneficiaries.get.head.incomeDistribution.isIncomeAtTrusteeDiscretion
        }

        "we have beneficiaryShareOfIncome details  " in {
          val unidentifiedBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "unidentified") (0)
          (unidentifiedBeneficaryList \ "beneficiaryShareOfIncome").get.as[String] mustBe
            String.valueOf(domainTrust.trustType.employmentTrust.get.beneficiaries.unidentifiedBeneficiaries.get.head.incomeDistribution.shareOfIncome.get)
        }

        "we have no beneficiaryShareOfIncome " in {
          val unidentifiedBeneficiaryWithoutShareOfIncome = unidentifiedBeneficiary.copy(incomeDistribution = incomeDistribution.copy(shareOfIncome = None, isIncomeAtTrusteeDiscretion = true))
          val empTrustWithUnidentifiedBeneficiary = Some(EmploymentTrust(assets,Beneficiaries(unidentifiedBeneficiaries = Some(List(unidentifiedBeneficiaryWithoutShareOfIncome))),Some(true),Some(new DateTime("1900-01-01"))))
          val domainTrust = trustWithEmploymentTrust.copy(trustType = TrustType(employmentTrust=empTrustWithUnidentifiedBeneficiary))
          val json = Json.toJson(domainTrust)(Trust.trustWrites)
          val unidentifiedBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "unidentified") (0)

          (unidentifiedBeneficaryList \ "beneficiaryShareOfIncome").validate[String].isError mustBe true
        }
      }

      "we have large type beneficiaries" when {
        val willIntestacyTrustWithLargeBeneficiary =  WillIntestacyTrust(assets,Beneficiaries(largeNumbersCompanyBeneficiaries = Some(List(largeNumbersCompanyBeneficiary))), deceased, false)
        val domainTrust = trustWithWillIntestacyTrust.copy(trustType = TrustType(willIntestacyTrust = Some(willIntestacyTrustWithLargeBeneficiary)))
        val json = Json.toJson(domainTrust)(Trust.trustWrites)

        "we have organisationName  details" in {
          val largeNumberCompanyBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "large") (0)
          (largeNumberCompanyBeneficaryList \ "organisationName").get.as[String] mustBe domainTrust.trustType.willIntestacyTrust.get.beneficiaries.largeNumbersCompanyBeneficiaries.get.head.company.name
        }

        "we have a description" in {
          val largeNumberCompanyBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "large") (0)
          (largeNumberCompanyBeneficaryList \ "description").get.as[String] mustBe domainTrust.trustType.willIntestacyTrust.get.beneficiaries.largeNumbersCompanyBeneficiaries.get.head.description
        }

        "we have numberOfBeneficiary details" in {
          val largeNumberCompanyBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "large") (0)
          (largeNumberCompanyBeneficaryList \ "numberOfBeneficiary").get.as[String] mustBe String.valueOf(domainTrust.trustType.willIntestacyTrust.get.beneficiaries.largeNumbersCompanyBeneficiaries.get.head.numberOfBeneficiaries)
        }

        "we have identification details having address details" in {
          val largeNumberCompanyBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "large")(0)

          (largeNumberCompanyBeneficaryList \ "identification" \ "address" \ "line1").get.as[String] mustBe  domainTrust.trustType.willIntestacyTrust.get.beneficiaries.largeNumbersCompanyBeneficiaries.get.head.company.correspondenceAddress.line1
        }

        "we have identification details having UTR details" in {
          val largeNumberCompanyBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "large")(0)

          (largeNumberCompanyBeneficaryList \ "identification" \ "utr").get.as[String] mustBe  domainTrust.trustType.willIntestacyTrust.get.beneficiaries.largeNumbersCompanyBeneficiaries.get.head.company.referenceNumber.get
        }

        "we have identification details without having a UTR and we have an address" in {
          val domainTrust = trustWithWillIntestacyTrust.copy(trustType = TrustType(willIntestacyTrust = Some(WillIntestacyTrust(assets,Beneficiaries(largeNumbersCompanyBeneficiaries = Some(List(largeNumbersCompanyBeneficiary.copy(company = company.copy(referenceNumber = None))))), deceased, false))))
          val json = Json.toJson(domainTrust)(Trust.trustWrites)

          val largeNumberCompanyBeneficaryList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "large")(0)

          (largeNumberCompanyBeneficaryList \ "identification" \ "utr").validate[String].isError mustBe true
          (largeNumberCompanyBeneficaryList \ "identification" \ "address" \ "line1").get.as[String] mustBe  domainTrust.trustType.willIntestacyTrust.get.beneficiaries.largeNumbersCompanyBeneficiaries.get.head.company.correspondenceAddress.line1
        }
      }
    }
  }
}

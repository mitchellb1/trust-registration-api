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
import uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries.{Beneficiaries, IndividualBeneficiary}
import uk.gov.hmrc.trustapi.rest.resources.core.trusttypes.{EmploymentTrust, TrustType}
import uk.gov.hmrc.utils.ScalaDataExamples

class BeneficiaryMapperSpec extends PlaySpec with OneAppPerSuite with ScalaDataExamples {

  "Beneficiary Mapper" should {
    "Map a domain representation of beneficiaries to a valid JSON Representation of DES beneficiaries" when {
      "we have individual beneficiaries" when {
        "we have a name" in {
          val domainTrust = trustWithEmploymentTrust
          val json = Json.toJson(domainTrust)(Trust.trustWrites)

          val beneficiariesList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "individualDetails")(0)
          (beneficiariesList \ "name" \ "firstName").get.as[String] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.individualBeneficiaries.get.head.individual.givenName
          (beneficiariesList \ "name" \ "lastName").get.as[String] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.individualBeneficiaries.get.head.individual.familyName
        }

        "we have a name with otherName/middelName" in {
          val employmentTrust = Some(EmploymentTrust(assets,Beneficiaries(Some(List(IndividualBeneficiary(individualWithOtherName,false, incomeDistribution)))),Some(true),Some(new DateTime("1900-01-01"))))
          val domainTrust = trustWithEmploymentTrust.copy(trustType = TrustType(employmentTrust=employmentTrust))
          val json = Json.toJson(domainTrust)(Trust.trustWrites)

          val beneficiariesList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "individualDetails")(0)
          (beneficiariesList \ "name" \ "middleName").get.as[String] mustBe domainTrust.trustType.employmentTrust.get.beneficiaries.individualBeneficiaries.get.head.individual.otherName.get
        }

        "we have date of birth details" in {
          val domainTrust = trustWithEmploymentTrust
          val json = Json.toJson(domainTrust)(Trust.trustWrites)
          println(json)
          val beneficiariesList = (json \ "details" \ "trust" \ "entities" \ "beneficiary" \ "individualDetails")(0)
          //TODO
          //(beneficiariesList \ "dateOfBirth").get.as[String] mustBe "1900-01-01"
        }
      }
    }
  }
}

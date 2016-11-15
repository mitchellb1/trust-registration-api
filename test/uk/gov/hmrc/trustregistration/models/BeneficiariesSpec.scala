/*
 * Copyright 2016 HM Revenue & Customs
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

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import uk.gov.hmrc.trustregistration.{JsonExamples, ScalaDataExamples}

class BeneficiariesSpec extends PlaySpec with JsonExamples with ScalaDataExamples {

  val invalidBeneficiariesError = "Must have at least one beneficiary"

  "Beneficiaries" must {
    "throw an exception" when {
      "all beneficiary options are none" in {
        val ex = the[IllegalArgumentException] thrownBy Beneficiaries()
        ex.getMessage() contains invalidBeneficiariesError
      }
      "an individual beneficiaries option exist but it's an empty list" in {
        val ex = the[IllegalArgumentException] thrownBy Beneficiaries(individualBeneficiaries = Some(List[IndividualBeneficiary]()))
        ex.getMessage() contains invalidBeneficiariesError
      }
      "a charity beneficiaries option exists but it's an empty list" in {
        val ex = the[IllegalArgumentException] thrownBy Beneficiaries(charityBeneficiaries = Some(List[CharityBeneficiary]()))
        ex.getMessage() contains invalidBeneficiariesError
      }
      "an other beneficiaries option exists but it's an empty list" in {
        val ex = the[IllegalArgumentException] thrownBy Beneficiaries(otherBeneficiaries = Some(List[OtherBeneficiary]()))
        ex.getMessage() contains invalidBeneficiariesError
      }
    }
    "exclude none values from the serialized response" in {
      val indBeneficiaries = new Beneficiaries(individualBeneficiaries = Some(List(individualBeneficiary)))
      val details = Json.toJson[Beneficiaries](indBeneficiaries).toString()
      details mustNot include ("charityBeneficiaries")
    }
  }

}
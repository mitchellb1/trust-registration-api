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

package uk.gov.hmrc.trustregistration.models.trusttypes

import org.scalatestplus.play.PlaySpec
import uk.gov.hmrc.trustregistration.models.assets.Assets
import uk.gov.hmrc.trustregistration.models.beneficiaries.Beneficiaries
import uk.gov.hmrc.trustregistration.{JsonExamples, ScalaDataExamples}

class InterVivoTrustSpec extends PlaySpec with ScalaDataExamples with JsonExamples{
  "InterVivoTrust" must{
    "throw an exception" when{
      "there are no assets" in {
        val assets = Assets()
        val beneficiaries = Beneficiaries(individualBeneficiaries = Some(List(individualBeneficiary)))
        val ex = the[IllegalArgumentException] thrownBy (InterVivoTrust(assets, beneficiaries, true, Some("dovTypeAbsolute")))
        ex.getMessage() mustEqual  "requirement failed: Must have at least one type of required Asset"
      }

      "there are no beneficiaries" in {
        val assets = Assets(otherAssets = Some(List(otherAsset)))
        val beneficiaries = Beneficiaries()
        val ex = the[IllegalArgumentException] thrownBy (InterVivoTrust(assets, beneficiaries, true, Some("dovTypeAbsolute")))
        ex.getMessage() mustEqual  "requirement failed: Must have at least one type of required Beneficiary"
      }

      "the correct beneficiaries are defined but they are all empty" in {
        val assets = Assets(otherAssets = Some(List(otherAsset)))
        val beneficiaries = Beneficiaries(
          individualBeneficiaries = Some(Nil),
          charityBeneficiaries = Some(Nil),
          trustBeneficiaries = Some(Nil),
          unidentifiedBeneficiaries = Some(Nil),
          otherBeneficiaries = Some(Nil),
          companyBeneficiaries = Some(Nil),
          largeNumbersCompanyBeneficiaries = Some(Nil)
        )
        val ex = the[IllegalArgumentException] thrownBy (InterVivoTrust(assets, beneficiaries, true, Some("dovTypeAbsolute")))
        ex.getMessage() mustEqual  "requirement failed: Must have at least one type of required Beneficiary"
      }

      "an employee beneficiary is defined" in {
        val assets = Assets(monetaryAssets = Some(List(2,2)))
        val beneficiaries = Beneficiaries(
          individualBeneficiaries = Some(List(individualBeneficiary)),
          employeeBeneficiaries = Some(List(employeeBeneficiary))
        )
        val ex = the[IllegalArgumentException] thrownBy (InterVivoTrust(assets, beneficiaries, true, Some("dovTypeAbsolute")))
        ex.getMessage() mustEqual  "requirement failed: Must have no other types of Beneficiary"
      }

      "a director beneficiary is defined" in {
        val assets = Assets(monetaryAssets = Some(List(2,2)))
        val beneficiaries = Beneficiaries(
          individualBeneficiaries = Some(List(individualBeneficiary)),
          directorBeneficiaries = Some(List(directorBeneficiary))
        )
        val ex = the[IllegalArgumentException] thrownBy (InterVivoTrust(assets, beneficiaries, true, Some("dovTypeAbsolute")))
        ex.getMessage() mustEqual  "requirement failed: Must have no other types of Beneficiary"
      }

      "when isHoldOverClaimed flag is set to false" in {
        val assets = Assets(monetaryAssets = Some(List(2,2)))
        val beneficiaries = Beneficiaries(individualBeneficiaries = Some(List(individualBeneficiary)))
        val ex = the[IllegalArgumentException] thrownBy (InterVivoTrust(assets, beneficiaries, false, Some("dovTypeAbsolute")))
        ex.getMessage() must include("isHoldOverClaimed must be true")
      }

      "when Inter Vivo Trust is created by a deed of variation and it has got a Partnership asset" in {
        val assets = Assets(partnershipAssets = Some(List(partnershipAsset)))
        val beneficiaries = Beneficiaries(individualBeneficiaries = Some(List(individualBeneficiary)))
        val ex = the[IllegalArgumentException] thrownBy (InterVivoTrust(assets, beneficiaries, true, Some("dovTypeAbsolute")))
        ex.getMessage() must include("partnership assets not allowed when Inter Vivo Trust is created by a deed of variation")
      }
    }

    "not throw an exception" when {
      "there is one asset" in {
        val assets = Assets(monetaryAssets = Some(List(2,2)))
        val beneficiaries = Beneficiaries(individualBeneficiaries = Some(List(individualBeneficiary)))
        noException should be thrownBy (InterVivoTrust(assets, beneficiaries, true, Some("dovTypeAbsolute")))
      }

      "there is more than one type of asset" in {
        val assets = Assets(monetaryAssets = Some(List(2,2)), otherAssets = Some(List(otherAsset)))
        val beneficiaries = Beneficiaries(individualBeneficiaries = Some(List(individualBeneficiary)))
        noException should be thrownBy (InterVivoTrust(assets, beneficiaries, true, Some("dovTypeAbsolute")))
      }

      "there is a partnership asset" in {
        val assets = Assets(
          partnershipAssets = Some(List(partnershipAsset))
        )
        val beneficiaries = Beneficiaries(individualBeneficiaries = Some(List(individualBeneficiary)))
        noException should be thrownBy (InterVivoTrust(assets, beneficiaries, true, None))
      }

      "there is a large number companies beneficiary" in {
        val assets = Assets(
          monetaryAssets = Some(List(2,2))
        )
        val beneficiaries = Beneficiaries(largeNumbersCompanyBeneficiaries = Some(List(largeNumbersCompanyBeneficiary)))
        noException should be thrownBy (InterVivoTrust(assets, beneficiaries, true, Some("dovTypeAbsolute")))
      }
    }
  }
}

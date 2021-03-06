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

package uk.gov.hmrc.models

import org.scalatestplus.play.PlaySpec
import uk.gov.hmrc.models.assets.Assets
import uk.gov.hmrc.models.beneficiaries.Beneficiaries
import uk.gov.hmrc.models.trusttypes.WillIntestacyTrust
import uk.gov.hmrc.utils.ScalaDataExamples

class WillIntestacyTrustSpec extends PlaySpec with ScalaDataExamples {

  "WillIntestacyTrust" must {
    "throw an exception" when {
      "there are no assets" in {
        val assets = Assets(None, None, None, None, None, None)
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)), None, None, None, None, None, None, None)
        val ex = the[IllegalArgumentException] thrownBy (WillIntestacyTrust(assets, beneficiaries, deceased, true))
        ex.getMessage() must include("Must have at least one type of required Asset")
      }

      "there are no beneficiaries" in {
        val assets = Assets(None, None, None, None, None, Some(List(otherAsset)))
        val beneficiaries = Beneficiaries(None, None, None, None, None, None, None, None)
        val ex = the[IllegalArgumentException] thrownBy (WillIntestacyTrust(assets, beneficiaries, deceased, true))
        ex.getMessage() must include("Must have at least one type of required Beneficiary")
      }

      "the wrong Beneficiaries are defined" in {
        val assets = Assets(Some(List(2, 2)), None, None, None, None, None)
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)), Some(List(employeeBeneficiary)), Some(List(directorBeneficiary)), None, None, None, None, None)
        val ex = the[IllegalArgumentException] thrownBy (WillIntestacyTrust(assets, beneficiaries, deceased, true))
        ex.getMessage() must include("Must have no other types of Beneficiary")
      }

      "the wrong assets are defined" in {
        val assets = Assets(Some(List(2, 2)), None, Some(List(shareAsset)), Some(List(partnershipAsset)), None, None)
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)), None, None, None, None, None, None, None)
        val ex = the[IllegalArgumentException] thrownBy (WillIntestacyTrust(assets, beneficiaries, deceased, true))
        ex.getMessage() must include("Must have no other types of Asset")
      }
    }

    "not throw an exception" when {
      "there is one asset" in {
        val assets = Assets(Some(List(2, 2)))
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)), None, None, None, None, None, None, None)
        noException should be thrownBy (WillIntestacyTrust(assets, beneficiaries, deceased, true))
      }

      "there is more than one type of asset" in {
        val assets = Assets(Some(List(2, 2)), None, None, None, None, Some(List(otherAsset)))
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)), None, None, None, None, None, None, None, Some(List(largeNumbersCompanyBeneficiary)))
        noException should be thrownBy (WillIntestacyTrust(assets, beneficiaries, deceased, true))
      }

      "only a large numbers company beneficiary is supplied" in {
        val assets = Assets(Some(List(2, 2)), None, None, None, None, Some(List(otherAsset)))
        val beneficiaries = Beneficiaries(largeNumbersCompanyBeneficiaries = Some(List(largeNumbersCompanyBeneficiary)))
        noException should be thrownBy (WillIntestacyTrust(assets, beneficiaries, deceased, true))
      }
    }
  }
}

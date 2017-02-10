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

import org.scalatestplus.play.PlaySpec
import uk.gov.hmrc.trustregistration.ScalaDataExamples

class WillIntestacyTrustSpec extends PlaySpec with ScalaDataExamples{
  "WillIntestacyTrust" must{
    "throw an exception" when{
      "there is no assets" in {
        val assets = Assets(None)
        val ex = the[IllegalArgumentException] thrownBy (WillIntestacyTrust(assets,beneficiaries))
        ex.getMessage() mustEqual  "requirement failed: Must have at least one type of Asset"
      }

      "the wrong Beneficiaries are defined" in {
        val assets = Assets(Some(List(2.0f,2.5f)))

        val beneficiaries = Beneficiaries(None,Some(List(employeeBeneficiary)))

        val ex = the[IllegalArgumentException] thrownBy WillIntestacyTrust(assets,beneficiaries)
        ex.getMessage() mustEqual  "requirement failed: Must have at least one required Beneficiary"
      }

      "no assets are defined" in {
        val ex = the[IllegalArgumentException] thrownBy (WillIntestacyTrust(Assets(), beneficiaries))
        ex.getMessage() mustEqual  "requirement failed: Must have at least one type of Asset"
      }
    }

    "not throw an exception" when {
      "there is one asset" in {
        val assets = Assets(Some(List(2.0f,2.5f)))
        noException should be thrownBy (WillIntestacyTrust(assets,beneficiaries))
      }

      "there is more than one type of asset" in {
        val assets = Assets(Some(List(2.0f,2.5f)),None,None,None,None,Some(List(otherAsset)))
        noException should be thrownBy (WillIntestacyTrust(assets,beneficiaries))
      }
    }
  }
}

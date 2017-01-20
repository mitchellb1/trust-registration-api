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

import org.joda.time.DateTime
import org.scalatestplus.play.PlaySpec
import uk.gov.hmrc.trustregistration.ScalaDataExamples


class FlatManagementSinkingFundTrustSpec extends PlaySpec with ScalaDataExamples {

  "Flat Management Sinking Fund Trust" must {
    "throw an exception" when{
      "no assets are defined" in {
        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(Assets(), beneficiaries)
        ex.getMessage mustEqual  "requirement failed: Must have at least one Monetary Asset"
      }

      "a property asset is defined" in {
        val assets = Assets(
          propertyAssets = Some(List(PropertyAsset(address, 1f)))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, beneficiaries)
        ex.getMessage mustEqual  "requirement failed: Only monetary assets are allowed"
      }

      "a share asset is defined" in {
        val assets = Assets(
          shareAssets = Some(List(ShareAsset(1, "Test", "Test", "Test", 5.0f)))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, beneficiaries)
        ex.getMessage mustEqual  "requirement failed: Only monetary assets are allowed"
      }

      "a partnership asset is defined" in {
        val assets = Assets(
          partnershipAssets = Some(List(PartnershipAsset("Test", "Test", DateTime.now)))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, beneficiaries)
        ex.getMessage mustEqual  "requirement failed: Only monetary assets are allowed"
      }

      "a business asset is defined" in {
        val assets = Assets(
          businessAssets = Some(List(BusinessAsset("Test", "Test", "Test", address, 1000f)))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, beneficiaries)
        ex.getMessage mustEqual  "requirement failed: Only monetary assets are allowed"
      }

      "an other asset is defined" in {
        val assets = Assets(
          otherAssets = Some(List(OtherAsset("Test",5.0f)))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, beneficiaries)
        ex.getMessage mustEqual  "requirement failed: Only monetary assets are allowed"
      }
    }

    "not throw an exception" when {
      "there is a valid monetary asset" in {
        val assets = Assets(monetaryAssets = Some(List(2.0f, 2.5f)))
        noException should be thrownBy FlatManagementSinkingFundTrust(assets,beneficiaries)
      }
    }
  }
}

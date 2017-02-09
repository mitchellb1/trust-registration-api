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

      // assets

      "no assets are defined" in {
        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(Assets() , Beneficiaries(otherBeneficiaries = otherBeneficiaries))
        ex.getMessage mustEqual  "requirement failed: Must have at least one monetary asset"
      }

      "a property asset is defined" in {
        val assets = Assets(
          propertyAssets = Some(List(PropertyAsset(address, 1L)))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets , Beneficiaries(otherBeneficiaries = otherBeneficiaries))
        ex.getMessage mustEqual  "requirement failed: Only monetary assets are allowed"
      }

      "a share asset is defined" in {
        val assets = Assets(
          shareAssets = Some(List(ShareAsset(1234,"shareCompanyName","shareCompanyRegistrationNumber","shareClass","shareType",123400.00f)))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets , Beneficiaries(otherBeneficiaries = otherBeneficiaries))
        ex.getMessage mustEqual  "requirement failed: Only monetary assets are allowed"
      }

      "a partnership asset is defined" in {
        val assets = Assets(
          partnershipAssets = Some(List(PartnershipAsset("Test", "Test", DateTime.now)))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets , Beneficiaries(otherBeneficiaries = otherBeneficiaries))
        ex.getMessage mustEqual  "requirement failed: Only monetary assets are allowed"
      }

      "a business asset is defined" in {
        val assets = Assets(
          businessAssets = Some(List(BusinessAsset(None, None, "Test", None, 1000, company)))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets , Beneficiaries(otherBeneficiaries = otherBeneficiaries))
        ex.getMessage mustEqual  "requirement failed: Only monetary assets are allowed"
      }

      "an other asset is defined" in {
        val assets = Assets(
          otherAssets = Some(List(otherAsset))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets , Beneficiaries(otherBeneficiaries = otherBeneficiaries))
        ex.getMessage mustEqual  "requirement failed: Only monetary assets are allowed"
      }

      // beneficiaries

      "no beneficiaries are defined" in {
        val assets = Assets(monetaryAssets = Some(List(2.0f, 2.5f)))

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries())
        ex.getMessage mustEqual  "requirement failed: Must have at least one beneficiary"
      }

      "an empty list of other beneficiaries are sent" in {
        val assets = Assets(monetaryAssets = Some(List(2.0f, 2.5f)))

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries(otherBeneficiaries = Some(List())))
        ex.getMessage mustEqual  "requirement failed: Must have at least one beneficiary"
      }

      "a individual beneficiaries is defined" in {
        val assets = Assets(monetaryAssets = Some(List(2.0f, 2.5f)))

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets , Beneficiaries(individualBeneficiaries = Some(List(individualBeneficiary))))
        ex.getMessage mustEqual  "requirement failed: Only other beneficiaries are allowed"
      }

      "a employee beneficiaries is defined" in {
        val assets = Assets(monetaryAssets = Some(List(2.0f, 2.5f)))

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets , Beneficiaries(employeeBeneficiaries = Some(List(employeeBeneficiary))))
        ex.getMessage mustEqual  "requirement failed: Only other beneficiaries are allowed"
      }

      "a director beneficiaries is defined" in {
        val assets = Assets(monetaryAssets = Some(List(2.0f, 2.5f)))

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets , Beneficiaries(directorBeneficiaries = Some(List(directorBeneficiary))))
        ex.getMessage mustEqual  "requirement failed: Only other beneficiaries are allowed"
      }

      "a charity beneficiaries is defined" in {
        val assets = Assets(monetaryAssets = Some(List(2.0f, 2.5f)))

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets , Beneficiaries(charityBeneficiaries = Some(List(charityBeneficiary))))
        ex.getMessage mustEqual  "requirement failed: Only other beneficiaries are allowed"
      }

    }

    "not throw an exception" when {
      "there is a valid monetary asset and a valid other beneficiary" in {
        val assets = Assets(monetaryAssets = Some(List(2.0f, 2.5f)))
        noException should be thrownBy FlatManagementSinkingFundTrust(assets , Beneficiaries(otherBeneficiaries = otherBeneficiaries))
      }
    }
  }
}

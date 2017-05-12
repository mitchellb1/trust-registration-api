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
import uk.gov.hmrc.trustapi.rest.resources.core.assets.{Assets, PropertyAsset, ShareAsset}
import uk.gov.hmrc.trustapi.rest.resources.core.beneficiaries.Beneficiaries
import uk.gov.hmrc.trustapi.rest.resources.core.trusttypes.FlatManagementSinkingFundTrust
import uk.gov.hmrc.trustregistration.ScalaDataExamples

class FlatManagementSinkingFundTrustSpec extends PlaySpec with ScalaDataExamples {

  "Flat Management Sinking Fund Trust" must {
    "throw an exception" when {

      // assets

      "no assets are defined" in {
        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(Assets(None), Beneficiaries(otherBeneficiaries = otherBeneficiaries))
        ex.getMessage must include("Must have at least one type of required Asset")
      }

      "a property asset is defined" in {

        val assets = Assets(
          monetaryAssets = Some(List(2, 2)),
          propertyAssets = Some(List(PropertyAsset(address, 1L)))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries(otherBeneficiaries = otherBeneficiaries))
        ex.getMessage must include("Must have no other types of Asset")
      }

      "a share asset is defined" in {
        val assets = Assets(
          monetaryAssets = Some(List(2, 2)),
          shareAssets = Some(List(ShareAsset(1234, "shareCompanyName", "shareCompanyRegistrationNumber", "shareClass", "shareType", 123400)))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries(otherBeneficiaries = otherBeneficiaries))
        ex.getMessage must include("Must have no other types of Asset")
      }

      "a partnership asset is defined" in {
        val assets = Assets(
          monetaryAssets = Some(List(2, 2)),
          partnershipAssets = Some(List(partnershipAsset))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries(otherBeneficiaries = otherBeneficiaries))
        ex.getMessage must include("Must have no other types of Asset")
      }

      "a business asset is defined" in {
        val assets = Assets(
          monetaryAssets = Some(List(2, 2)),
          businessAssets = Some(List(businessAsset))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries(otherBeneficiaries = otherBeneficiaries))
        ex.getMessage must include("Must have no other types of Asset")
      }

      "an other asset is defined" in {
        val assets = Assets(
          monetaryAssets = Some(List(2, 2)),
          otherAssets = Some(List(otherAsset))
        )

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries(otherBeneficiaries = otherBeneficiaries))
        ex.getMessage must include("Must have no other types of Asset")
      }

      // beneficiaries

      "no beneficiaries are defined" in {
        val assets = Assets(monetaryAssets = Some(List(2, 2)))
        val beneficiaries = Beneficiaries(None, None, None, None, None, None, None, None)
        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries())
        ex.getMessage must include("Must have at least one type of required Beneficiary")
      }

      "an empty list of other beneficiaries are sent" in {
        val assets = Assets(monetaryAssets = Some(List(2, 2)))

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries(otherBeneficiaries = Some(List())))
        ex.getMessage must include("Must have at least one type of required Beneficiary")
      }

      "a individual beneficiaries is defined" in {
        val assets = Assets(monetaryAssets = Some(List(2, 2)))

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries(otherBeneficiaries = otherBeneficiaries, individualBeneficiaries = Some(List(individualBeneficiary))))
        ex.getMessage must include("Must have no other types of Beneficiary")
      }

      "a large number company beneficiary is defined" in {
        val assets = Assets(monetaryAssets = Some(List(2, 2)))

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries(otherBeneficiaries = otherBeneficiaries, largeNumbersCompanyBeneficiaries = Some(List(largeNumbersCompanyBeneficiary))))
        ex.getMessage must include("Must have no other types of Beneficiary")
      }

      "a employee beneficiaries is defined" in {
        val assets = Assets(monetaryAssets = Some(List(2, 2)))

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries(otherBeneficiaries = otherBeneficiaries, employeeBeneficiaries = Some(List(employeeBeneficiary))))
        ex.getMessage must include("Must have no other types of Beneficiary")
      }

      "a director beneficiaries is defined" in {
        val assets = Assets(monetaryAssets = Some(List(2, 2)))

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries(otherBeneficiaries = otherBeneficiaries, directorBeneficiaries = Some(List(directorBeneficiary))))
        ex.getMessage must include("Must have no other types of Beneficiary")
      }

      "a charity beneficiaries is defined" in {
        val assets = Assets(monetaryAssets = Some(List(2, 2)))

        val ex = the[IllegalArgumentException] thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries(otherBeneficiaries = otherBeneficiaries, charityBeneficiaries = Some(List(charityBeneficiary))))
        ex.getMessage must include("Must have no other types of Beneficiary")
      }

    }

    "not throw an exception" when {
      "there is a valid monetary asset and a valid other beneficiary" in {
        val assets = Assets(monetaryAssets = Some(List(2, 2)))
        noException should be thrownBy FlatManagementSinkingFundTrust(assets, Beneficiaries(otherBeneficiaries = otherBeneficiaries))
      }
    }
  }
}

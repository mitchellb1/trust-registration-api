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


class HeritageMaintenanceFundTrustSpec extends PlaySpec with ScalaDataExamples {
  "Heritage Maintenance Fund Trust" must {
    "throw an exception" when{
      "there is no assets" in {
        val assets = Assets(None)
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)),None,None,None,None,None,None,None)
        val ex = the[IllegalArgumentException] thrownBy (HeritageMaintenanceFundTrust(assets,beneficiaries,true))
        ex.getMessage() mustEqual ("requirement failed: Must have at least one type of required Asset")
      }

      "a partnership asset is defined" in {
        val assets =    Assets(Some(List(2,2)), None, None, Some(List(partnershipAsset)), None, None)
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)),None,None,None,None,None,None,None)
        val ex = the[IllegalArgumentException] thrownBy (HeritageMaintenanceFundTrust(assets,beneficiaries,true))
        ex.getMessage() mustEqual ("requirement failed: Must have no other types of Asset")
      }

      "a business asset is defined" in {
        val assets = Assets(Some(List(2,2)), None, None, None, Some(List(businessAsset)), None)
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)),None,None,None,None,None,None,None)
        val ex = the[IllegalArgumentException] thrownBy (HeritageMaintenanceFundTrust(assets,beneficiaries,true))
        ex.getMessage() mustEqual ("requirement failed: Must have no other types of Asset")
      }

      "the required beneficiaries are not there" in {
        val assets = Assets(Some(List(2,2)))
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)),None,None,None,None,None,None,None)
        val ex = the[IllegalArgumentException] thrownBy (HeritageMaintenanceFundTrust(assets,beneficiaries,true))
        ex.getMessage() mustEqual  ("requirement failed: Must have at least one type of required Beneficiary")
      }

      "no assets are defined" in {
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)),None,None,None,None,None,None,None)
        val ex = the[IllegalArgumentException] thrownBy (HeritageMaintenanceFundTrust(Assets(), beneficiaries, true))
        ex.getMessage() mustEqual  "requirement failed: Must have at least one type of required Asset"
      }
    }

    "not throw an exception" when {
      "there is one asset" in {
        val assets = Assets(Some(List(2,2)))
        val beneficiaries = Beneficiaries(None,None,None,None,otherBeneficiaries,None,None,None)
        noException should be thrownBy (HeritageMaintenanceFundTrust(assets,beneficiaries,true))
      }

      "there is more than one type of asset" in {
        val assets = Assets(Some(List(2,2)),None,None,None,None,Some(List(otherAsset)))
        val beneficiaries = Beneficiaries(None,None,None,None,otherBeneficiaries,None,None,None)
        noException should be thrownBy (HeritageMaintenanceFundTrust(assets,beneficiaries,true))
      }

      "there is other asset defined" in {
        val assets = Assets(Some(List(2,2)),None,None,None,None,Some(List(otherAsset)))
        val beneficiaries = Beneficiaries(None,None,None,None,otherBeneficiaries,None,None,None)
        noException should be thrownBy (HeritageMaintenanceFundTrust(assets,beneficiaries,true))
      }
    }
  }
}

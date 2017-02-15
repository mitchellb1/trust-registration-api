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
import uk.gov.hmrc.trustregistration.models.assets.Assets
import uk.gov.hmrc.trustregistration.models.beneficiaries.Beneficiaries
import uk.gov.hmrc.trustregistration.models.trusttypes.InterVivoTrust
import uk.gov.hmrc.trustregistration.{JsonExamples, ScalaDataExamples}


class InterVivoTrustSpec extends PlaySpec with ScalaDataExamples with JsonExamples{
  "InterVivoTrust" must{
    "throw an exception" when{
      "there are no assets" in {
        val assets = Assets(None,None,None,None,None,None)
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)),None,None,None,None,None,None,None)
        val ex = the[IllegalArgumentException] thrownBy (InterVivoTrust(assets,beneficiaries, true, Some("Dovtypeabsolute")))
        ex.getMessage() mustEqual  "requirement failed: Must have at least one type of required Asset"
      }

      "there are no beneficiaries" in {
        val assets = Assets(None,None,None,None,None,Some(List(otherAsset)))
        val beneficiaries = Beneficiaries(None,None,None,None,None,None,None,None)
        val ex = the[IllegalArgumentException] thrownBy (InterVivoTrust(assets,beneficiaries, true, Some("Dovtypeabsolute")))
        ex.getMessage() mustEqual  "requirement failed: Must have at least one type of required Beneficiary"
      }

      "the wrong Beneficiaries are defined" in {
        val assets = Assets(Some(List(2,2)),None,None,None,None,None)
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)), Some(List(employeeBeneficiary)), Some(List(directorBeneficiary)), None, None, None, None, None)
        val ex = the[IllegalArgumentException] thrownBy (InterVivoTrust(assets,beneficiaries, true, Some("Dovtypeabsolute")))
        ex.getMessage() mustEqual  "requirement failed: Must have no other types of Beneficiary"
      }
    }

    "not throw an exception" when {
      "there is one asset" in {
        val assets = Assets(Some(List(2,2)))
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)),None,None,None,None,None,None,None)
        noException should be thrownBy (InterVivoTrust(assets, beneficiaries, true, Some("Dovtypeabsolute")))
      }

      "there is more than one type of asset" in {
        val assets = Assets(Some(List(2,2)),None,None,None,None,Some(List(otherAsset)))
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)),None,None,None,None,None,None,None)
        noException should be thrownBy (InterVivoTrust(assets, beneficiaries, true, Some("Dovtypeabsolute")))
      }

      "has a partnership Asset" in {
        val assets = Assets(Some(List(2,2)),None,Some(List(shareAsset)),Some(List(partnershipAsset)),None,None)
        val beneficiaries = Beneficiaries(Some(List(individualBeneficiary)),None,None,None,None,None,None,None)
        noException should be thrownBy (InterVivoTrust(assets, beneficiaries, true, Some("Dovtypeabsolute")))
      }
    }
  }
}

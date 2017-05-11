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
import uk.gov.hmrc.trustapi.rest.resources.core.trusttypes.TrustType
import uk.gov.hmrc.trustregistration.ScalaDataExamples

class TrustTypeSpec extends PlaySpec with ScalaDataExamples {
  "TrustType" must {
    "throw an exception" when {
      "no trust type is defined" in {
        val ex = the[IllegalArgumentException] thrownBy TrustType()

        ex.getMessage must include("Must have a Trust type")
      }

      "more than one trust type is defined" in {
        val ex = the[IllegalArgumentException] thrownBy TrustType(
          willIntestacyTrust = Some(willIntestacyTrust),
          heritageMaintenanceFundTrust = heritageFund
        )

        ex.getMessage must include("Must have only one Trust type")
      }
    }
  }
}

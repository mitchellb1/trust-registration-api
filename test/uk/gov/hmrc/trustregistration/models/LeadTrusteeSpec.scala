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
import uk.gov.hmrc.trustregistration.{JsonExamples, ScalaDataExamples}

class LeadTrusteeSpec extends PlaySpec with JsonExamples with ScalaDataExamples {

  "Lead Trustee" must {
    "throw an exception" when {
      "there are is no individual or company assigned" in {
        val ex = the [IllegalArgumentException] thrownBy (LeadTrustee(None, None))
        ex.getMessage() mustEqual  ("requirement failed: Must have either an individual or company lead trustee")
      }
      "there are both an individual and company assigned" in {
        val ex = the [IllegalArgumentException] thrownBy (LeadTrustee(Some(individual), Some(company)))
        ex.getMessage() mustEqual  ("requirement failed: Must have only an individual or company lead trustee")
      }
    }
  }

}
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
import uk.gov.hmrc.trustapi.rest.resources.core.LeadTrustee
import uk.gov.hmrc.utils.ScalaDataExamples

class LeadTrusteeSpec extends PlaySpec with ScalaDataExamples {

  "LeadTrustee" must {
    "throw an exception" when {
      "no individual or company have been specified" in {
        val ex = the [IllegalArgumentException] thrownBy LeadTrustee(None, None, "12345", "test@test.com")
        ex.getMessage must include("Must have either an individual or company lead trustee")
      }
      "both individual and company have been specified" in {
        val ex = the [IllegalArgumentException] thrownBy LeadTrustee(Some(individual), Some(company), "12345", "test@test.com")
        ex.getMessage  must include("Must have only an individual or company lead trustee")
      }
    }
  }

}

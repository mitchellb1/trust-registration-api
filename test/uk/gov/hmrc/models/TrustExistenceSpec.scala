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
import uk.gov.hmrc.utils.JsonExamples


class TrustExistenceSpec extends PlaySpec with JsonExamples{
  "Trust Existence" must {
    "throw an exception" when {
      "there is no utr" in {
        val ex = the [IllegalArgumentException] thrownBy TrustExistence("test",None,None)
        ex.getMessage must include("Missing required property utr")
      }
    }
  }


}

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


class EstateSpec extends PlaySpec {
  "Estate" must {
    "throw an exception" when {
      "there are no personal representatives or deceased" in {
        val ex = the [IllegalArgumentException] thrownBy (Estate(true,true,true,true,None,None))
        ex.getMessage() mustEqual   ("requirement failed: Must have either a personal representative or a deceased")
      }
    }
  }
}

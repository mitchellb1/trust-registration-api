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
import play.api.libs.json.Json
import uk.gov.hmrc.models.beneficiaries.IncomeDistribution
import uk.gov.hmrc.utils.{JsonExamples, ScalaDataExamples}

class  IncomeSpec extends PlaySpec with JsonExamples with ScalaDataExamples {

  val validIncomeAtDiscretion = """{"isIncomeAtTrusteeDiscretion":true}"""

  val validIncomeNotAtDiscretion = """{"isIncomeAtTrusteeDiscretion":false, "shareOfIncome":50}"""


  "Income" must {
    "serialize from Json" when {
      "a valid Income At Discretion is sent" in {
        val income: IncomeDistribution = Json.parse(validIncomeAtDiscretion).as[IncomeDistribution]

        income.isIncomeAtTrusteeDiscretion mustBe true
      }
      "a valid Income Not At Discresion is sent" in {
        val income: IncomeDistribution = Json.parse(validIncomeNotAtDiscretion).as[IncomeDistribution]

        income.isIncomeAtTrusteeDiscretion mustBe false
      }
    }

    "throw an exception" when {
      "an inValid Income At Discretion is sent" in {
        val ex = the [IllegalArgumentException] thrownBy IncomeDistribution(true, Some(50))
        ex.getMessage must include("field not required")
      }

      "an inValid Not Income At Discretion is sent" in {
        val ex = the [IllegalArgumentException] thrownBy IncomeDistribution(false, None)
        ex.getMessage must include("missing field")
      }

      "an Valid Income At Discretion is sent" in {
        noException should be thrownBy (IncomeDistribution(true, None))
      }

      "an Valid Not Income At Discretion is sent" in {
        noException should be thrownBy (IncomeDistribution(false, Some(50)))
      }
    }
  }

}

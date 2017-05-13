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
import play.api.libs.json.{JsResultException, Json}
import uk.gov.hmrc.common.rest.resources.core.Deceased
import uk.gov.hmrc.utils.JsonExamples

class DeceasedSpec extends PlaySpec with JsonExamples {

  val invalidDeceasedJsonBadDate = s"""{"individual": $validIndividualJson, "dateOfDeath": "01/01/2000"}"""
  val invalidDeceasedJsonIndividual = s"""{"individual": $invalidIndividualJson, "dateOfDeath": "2000-01-01"}"""

  "Deceased" must {
    "serialize from Json" when {
      "all fields are correctly populated" in {
        val deceased: Deceased = Json.parse(validDeceasedJson).as[Deceased]

        deceased.individual.familyName mustBe "Spaceman"
        deceased.dateOfDeath mustBe new DateTime(2000, 1, 1, 0, 0)
      }
    }
    "throw an exception" when {
      "an incorrect date format is used" in {
        val ex = the[IllegalArgumentException] thrownBy Json.parse(invalidDeceasedJsonBadDate).as[Deceased]

        ex.getMessage must include ("""Invalid format: "01/01/2000" is malformed""")
      }
      "an invalid individual is used" in {
        val ex = the[JsResultException] thrownBy Json.parse(invalidDeceasedJsonIndividual).as[Deceased]

        ex.errors.head._1.toString() mustBe "/individual/dateOfBirth"
      }
    }
  }

}

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
import play.api.libs.json.Json
import uk.gov.hmrc.common.des.DesPartnership
import uk.gov.hmrc.utils.{JsonExamples, ScalaDataExamples}

class DesPartnershipSpec  extends PlaySpec with JsonExamples with ScalaDataExamples {
  "DesPartnership" must {
    "serialize from JSON" when {
      "a DesPartnership object is specified" in {
        val partnershipJSON = """{"utr": "12345239234", "type": "Partnership Trade", "partnershipStart":"20-01-1999"}"""
        val partnership = Json.parse(partnershipJSON).as[DesPartnership](DesPartnership.reads)

        partnership.partnershipType mustBe "Partnership Trade"
      }
    }
  }
}
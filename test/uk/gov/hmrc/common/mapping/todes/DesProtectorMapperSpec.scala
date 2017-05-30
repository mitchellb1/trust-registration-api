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

package uk.gov.hmrc.common.mapping.todes

import org.scalatestplus.play.PlaySpec
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DesProtectorMapperSpec extends PlaySpec
  with ScalaDataExamples
  with DesScalaExamples {

  "Des protector mapper" should {
    "map an invidual to a desprotector correctly" when {
      "we have an individuals name details" in {
        val output = DesProtectorMapper.toDes(individual)
        individual.givenName mustBe output.name.firstName
      }
      "we have an individual date of birth" in {
        val output = DesProtectorMapper.toDes(individual)
        individual.dateOfBirth mustBe output.dateOfBirth
      }
      "we have valid identificaion details" in {
        val output = DesProtectorMapper.toDes(individual)
        individual.correspondenceAddress.get.line1 mustBe output.identification.address.get.line1
      }
    }
  }
}



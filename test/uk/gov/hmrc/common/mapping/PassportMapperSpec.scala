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

package uk.gov.hmrc.common.mapping

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class PassportMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val output = PassportMapper.toDomain(desPassport)

  "Passport Mapper" must {
    "Map fields correctly to Domain Passport" when {
      "we have a number" in {
        output.referenceNumber mustBe desPassport.number
      }
      "we have a date" in {
        output.expiryDate mustBe desPassport.expirationDate
      }
      "we have a country of issue" in {
        output.countryOfIssue mustBe desPassport.countryOfIssue
      }
    }
  }
}
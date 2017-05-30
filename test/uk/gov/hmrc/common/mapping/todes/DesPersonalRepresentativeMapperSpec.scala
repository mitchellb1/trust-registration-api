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

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.common.des.DesPersonalRepresentative
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}

class DesPersonalRepresentativeMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  "Personal Representative Mapper" must {
    "Map fields correctly to Des Personal Representative" when {
      "we have a correct email from the Domain" in {
        val output: DesPersonalRepresentative = DesPersonalRepresentativeMapper.toDes(personalRepresentative)
        output.email mustBe Some(personalRepresentative.email)
      }
      "we have a correct phone number from  the Domain" in {
        val output: DesPersonalRepresentative = DesPersonalRepresentativeMapper.toDes(personalRepresentative)
        output.phoneNumber mustBe Some(personalRepresentative.telephoneNumber)
      }
      "we have valid data to create a des name from the Domain" in {
        val output: DesPersonalRepresentative = DesPersonalRepresentativeMapper.toDes(personalRepresentative)
        output.name.firstName mustBe personalRepresentative.individual.givenName
      }
    }
  }
}


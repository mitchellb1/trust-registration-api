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
import uk.gov.hmrc.common.des.DesName
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DesNameMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val SUT = DesNameMapper
  val domainIndividualToMap = individual

  "Name Mapper to Des" must {
    "map properties correctly" when {
      "we have a correct first name on des domain" in {
        val output: DesName = SUT.toDes(domainIndividualToMap)
        output.firstName mustBe domainIndividualToMap.givenName
      }
      "we have a correct middle name on des domain" in {
        val output: DesName = SUT.toDes(domainIndividualToMap)
        output.middleName mustBe domainIndividualToMap.otherName
      }
      "we have a correct last name on des domain" in {
        val output: DesName = SUT.toDes(domainIndividualToMap)
        output.lastName mustBe domainIndividualToMap.familyName
      }
    }
  }
}




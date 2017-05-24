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
import uk.gov.hmrc.common.des.DesIndividualDetails
import uk.gov.hmrc.common.rest.resources.core.Individual
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}

class DesIndividualDetailsMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val SUT = DesIndividualDetailsMapper
  val domainIndividualPassportToMap: Individual = individual
  val domainIndividualwithNinoToMap: Individual = individualwithNino

  "Des Identification Mapper" must {
    "map properties correctly" when {
      "we have a correct dateOfBirth in the our domain" in {
        val output: DesIndividualDetails = SUT.toDes(domainIndividualwithNinoToMap)
        output.dateOfBirth mustBe domainIndividualwithNinoToMap.dateOfBirth
      }
      "we have a name in our domain" in {
        val output: DesIndividualDetails = SUT.toDes(domainIndividualwithNinoToMap)
        output.name.lastName mustBe domainIndividualwithNinoToMap.familyName
        output.name.middleName mustBe domainIndividualwithNinoToMap.otherName
        output.name.firstName mustBe domainIndividualwithNinoToMap.givenName
      }
      "we have a no passport details in the our domain" in {
        val output: DesIndividualDetails = SUT.toDes(domainIndividualwithNinoToMap)
        output.identification.passport mustBe None
      }
    }
  }
}

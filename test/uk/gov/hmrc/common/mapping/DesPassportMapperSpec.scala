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
import uk.gov.hmrc.common.des.DesPassportType
import uk.gov.hmrc.common.rest.resources.core.Individual
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}

class DesPassportMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val SUT = DesPassportTypeMapper
  val domainIndividualPassportToMap: Individual = individual
  val domainIndividualwithNinoToMap: Individual = individualwithNino

  "Des PassportType Mapper to Des" must {
    "map properties correctly" when {
      "we have a correct referenceNumber on des domain" in {
        val output: Option[DesPassportType] = SUT.toDes(domainIndividualPassportToMap)
        output.get.number mustBe domainIndividualPassportToMap.passportOrIdCard.get.referenceNumber
      }
      "we have a correct expiryDate on des domain" in {
        val output: Option[DesPassportType] = SUT.toDes(domainIndividualPassportToMap)
        output.get.expirationDate mustBe domainIndividualPassportToMap.passportOrIdCard.get.expiryDate
      }
      "we have a correct countryOfIssue on des domain" in {
        val output: Option[DesPassportType] = SUT.toDes(domainIndividualPassportToMap)
        output.get.countryOfIssue mustBe domainIndividualPassportToMap.passportOrIdCard.get.countryOfIssue
      }
      "we don't have a passport on des domain" in {
        val output: Option[DesPassportType] = SUT.toDes(domainIndividualwithNinoToMap)
        output mustBe None
      }
    }
  }
}




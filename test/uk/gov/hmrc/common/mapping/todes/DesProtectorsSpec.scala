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
import uk.gov.hmrc.common.des.DesProtectorType
import uk.gov.hmrc.trustapi.rest.resources.core.Protectors
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DesProtectorsSpec extends PlaySpec with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples  {


  "Des protector mapper" should {
    "map a rest domain protector to des protector correctly" when {
      "we have a valid individual" in {
        val individualProtector: Protectors = protectors.copy(companies = None)
        val output = DesProtectorsMapper.toDes(individualProtector)

        protectors.individuals.get.head.givenName mustBe output.protector.get.head.name.firstName
      }
      "we have a valid company" in {
        val companyProtector : Protectors = protectors.copy(individuals = None)
        val output = DesProtectorsMapper.toDes(companyProtector)

        protectors.companies.get.head.name mustBe output.protectorCompany.get.head.name
      }
      "we have both a valid individual and a company" in {
        val companyProtector : Protectors = protectors
        val output = DesProtectorsMapper.toDes(companyProtector)

        protectors.companies.get.head.name mustBe output.protectorCompany.get.head.name
        protectors.individuals.get.head.givenName mustBe output.protector.get.head.name.firstName
      }
    }
  }
}


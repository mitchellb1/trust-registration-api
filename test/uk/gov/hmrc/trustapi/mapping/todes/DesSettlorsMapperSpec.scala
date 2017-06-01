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

package uk.gov.hmrc.trustapi.mapping.todes

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DesSettlorsMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples
  {
    "DES Settlor mapper" should {
      "map a rest domain settlor  to des settlor correctly" when {
        "we have an individual" in {
          val settlorsNoCompany = settlors.copy(companies = None)
          val output = DesSettlorTypeMapper.toDes(settlorsNoCompany)

          output.settlor.get.head.name.firstName mustBe settlorsNoCompany.individuals.get.head.givenName
        }

        "we have a valid company to create a des settlor company" when {
          val settlorsNoIndividual = settlors.copy(individuals = None)
          val output = DesSettlorTypeMapper.toDes(settlorsNoIndividual)

          output.settlorCompany.get.head.name mustBe settlorsNoIndividual.companies.get.head.name
        }

        "we have both individual and company" when {
          val output = DesSettlorTypeMapper.toDes(settlors)

          output.settlor.get.head.name.firstName mustBe settlors.individuals.get.head.givenName
          output.settlorCompany.get.head.name mustBe settlors.companies.get.head.name
        }
      }
    }
  }



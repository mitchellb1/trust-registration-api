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

package uk.gov.hmrc.common.mapping.todomain

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.common.des.{DesSettlor, DesSettlorCompany, DesSettlorType}
import uk.gov.hmrc.trustapi.mapping.todomain.SettlorsMapper
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class SettlorsMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  "Settlors mapper" should {
    "map a des settlors to a domain settlors correctly" when {
      "we have a list of DES settlor individuals" in {
        val settlor = DesSettlor(desName,date,desIdentification)
        val settlors = DesSettlorType(Some(List(settlor,settlor,settlor,settlor)))
        val output = SettlorsMapper.toDomain(settlors)

        output.individuals.get.head.givenName mustBe settlors.settlor.get.head.name.firstName
      }

      "we have a list of DES settlor companies" in {
        val settlorcompany = DesSettlorCompany("Test","Investment",false,desOrgIdentification)
        val settlorscompany = DesSettlorType(None,Some(List(settlorcompany,settlorcompany,settlorcompany,settlorcompany)))
        val output = SettlorsMapper.toDomain(settlorscompany)

        output.settlorCompanies.get.head.company.referenceNumber mustBe settlorscompany.settlorCompany.get.head.identification.utr
        output.settlorCompanies.get.head.twoYearTrading mustBe settlorscompany.settlorCompany.get.head.companyTime
        output.settlorCompanies.get.head.typeOfSettlorCompany mustBe settlorscompany.settlorCompany.get.head.companyType
      }
    }
  }
}

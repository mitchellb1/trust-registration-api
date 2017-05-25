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
import uk.gov.hmrc.common.des.DesYearsReturns
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}

class DesYearReturnsMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val SUT = DesYearReturnsMapper
  val domainYearsOfTaxConsequenceToMap = yearsOfTaxConsequence
  val output: Option[DesYearsReturns] = SUT.toDes(domainYearsOfTaxConsequenceToMap)

  "Des YearReturns Mapper to Des" must {
    "map properties correctly" when {
      "we have a correct no dues on des domain" in {
        output.get.taxReturnsNoDues mustBe yearsOfTaxConsequence.get.taxReturnsNoDues
      }
      "we have a correct taxReturnYear on des domain" in {
        output.get.returns.get.lift(0).get.taxReturnYear mustBe yearsOfTaxConsequence.get.returns.get.lift(0).get.taxReturnYear

      }
      "we have a correct taxConsequence on des domain" in {
        output.get.returns.get.lift(0).get.taxConsequence mustBe yearsOfTaxConsequence.get.returns.get.lift(0).get.taxConsequence
      }
    }
  }
}




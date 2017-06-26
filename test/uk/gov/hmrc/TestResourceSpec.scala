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

package uk.gov.hmrc


import uk.gov.hmrc.models.{Individual, Passport}
import uk.gov.hmrc.play.test.UnitSpec

import scala.io.Source

class TestResourceSpec extends UnitSpec {

  val validPassportJson = Source.fromFile(getClass.getResource("/ValidPassport.json").getPath).mkString

  val validIndividualJson = Source
                              .fromFile(getClass.getResource("/ValidIndividual.json").getPath)
                              .mkString
                              .replace("\"{PASSPORT}\"", validPassportJson)
                              .replace("\"{ADDRESS}\"", "null")

  "Test Suites" should {

    "be able to pull in test resources" in {
      val result = Json.parse(validPassportJson).asOpt[Passport].get

      result.referenceNumber shouldBe "IDENTIFIER"
    }

    "be able to combine test resources" in {
      val result = Json.parse(validIndividualJson).asOpt[Individual].get

      result.givenName shouldBe "Leo"
      result.passportOrIdCard.get.referenceNumber shouldBe "IDENTIFIER"
    }

  }

}

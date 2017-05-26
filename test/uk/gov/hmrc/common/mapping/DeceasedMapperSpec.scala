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
import uk.gov.hmrc.common.des.DesWill
import uk.gov.hmrc.common.rest.resources.core.Deceased
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DeceasedMapperSpec extends PlaySpec
  with OneAppPerSuite
  with DesScalaExamples
  with ScalaDataExamples {

  "Deceased mapper" must {
    "Map correctly to a deceased domain" when {
      "we have enough data to map an individual" in {
        val desWill = DesWill(desName,date,date,desWillId)
        val output = DeceasedMapper.toDomain(desWill)
        output.individual.givenName mustBe desName.firstName
        output.individual.nino mustBe desWillId.nino
        output.individual.correspondenceAddress mustBe None
      }

      "we have a date of death" in {
        val desWill = DesWill(desName,date,date,desWillId)
        val output = DeceasedMapper.toDomain(desWill)
        output.dateOfDeath mustBe date
      }
    }
  }
}

object DeceasedMapper extends ScalaDataExamples with DesScalaExamples {
  def toDomain(desWill: DesWill) : Deceased = {
    Deceased(IndividualMapper.toDomain(desWill.name,desWill.dateOfBirth,desWillIdentification = Some(desWill.identification)), desWill.dateOfDeath)
  }
}

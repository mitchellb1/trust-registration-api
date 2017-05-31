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
import uk.gov.hmrc.common.des.DesSettlorType
import uk.gov.hmrc.trustapi.rest.resources.core.Settlors
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DesSettlorsMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples
  {
    "DES Settlor mapper" should {
      "map a rest domain settlor  to des settlor correctly" when {
        "we have an individual" in {
            val output = DesSettlorTypeMapper.toDes(settlors)
            output.settlor.get.head.name.firstName mustBe settlors.individuals.get.head.givenName
        }
      }
    }
  }

object DesSettlorTypeMapper {
  def toDes(settlors: Settlors) : DesSettlorType = {
    DesSettlorType(settlors.individuals.map(li=>li.map(i=>DesSettlorMapper.toDes(i))))
  }
}


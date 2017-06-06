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
import uk.gov.hmrc.common.des.DesTrustee
import uk.gov.hmrc.trustapi.mapping.todomain.TrusteesMapper
import uk.gov.hmrc.trustapi.rest.resources.core.Trustees
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DesTrusteesMapperSpec extends PlaySpec with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

//  "DesTrustees mapper" should {
//    "Map trustees correctly" when {
//      "when we have a trustee that can be mapped to an individual" in {
//        val trustees: Trustees = List(individual, individual)
//        val output: List[DesTrustee] = DesTrusteesMapper.toDes(trustees)
//
//        output.individual.get.familyName mustBe desTrustee.name.lastName
//      }
//    }
//  }
}



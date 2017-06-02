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

package uk.gov.hmrc.trustapi.mapping.todomain

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import uk.gov.hmrc.common.des._
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class ProtectorsMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  "Protectors mapper" should {
    "map a des protector to a domain protector correctly" when {
      "we have a list of desprotector" in {
        val protector = DesProtector(desName,date,desIdentification)
        val protectors = DesProtectorType(Some(List(protector,protector)))
        val output = ProtectorsMapper.toDomain(protectors)

        output.individuals.get.head.familyName mustBe protectors.protector.get.head.name.lastName
      }
      "we have a list of desprotector company" in {
        val protectorCompany = DesProtectorCompany("Test",desOrgIdentification)
        val protectors = DesProtectorType(protectorCompany = Some(List(protectorCompany,protectorCompany)))
        val output = ProtectorsMapper.toDomain(protectors)

        output.companies.get.head.referenceNumber mustBe protectors.protectorCompany.get.head.identification.utr
      }
    }
  }
}

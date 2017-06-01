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

import org.scalatestplus.play.PlaySpec
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DesSettlorCompanyMapperSpec extends PlaySpec
  with ScalaDataExamples
  with DesScalaExamples {

  "Des settlor company mapper" should {
    val companyType = "Trading"
    val output = DesSettlorCompanyMapper.toDes(company,companyType,false)

    "map a rest domain company to des protector company correctly" when {
      "we have a valid name" in {
        output.name mustBe company.name
      }

      "we have a company type" in {
        output.companyType mustBe companyType
      }

      "we have a companyTime flag" in {
        output.companyTime mustBe false
      }

      "we have a valid utr and address to create an org identification" in {
        output.identification.utr mustBe company.referenceNumber
      }
    }
  }
}


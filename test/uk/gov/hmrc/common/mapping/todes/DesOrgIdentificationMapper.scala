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

import org.scalatestplus.play.PlaySpec
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DesOrgIdentificationMapperSpec extends PlaySpec
  with ScalaDataExamples
  with DesScalaExamples {

  val output = DesOrgIdentificationMapper.toDes(company)

  "Des org identification mapper" should {
    "map a rest company to a org identificaiton succesfully" when {
      "we have a valid utr" in {
        output.utr mustBe company.referenceNumber
      }

      "we have a valid address" in {
        output.address.get.line1 mustBe company.correspondenceAddress.line1
      }
    }
  }
}



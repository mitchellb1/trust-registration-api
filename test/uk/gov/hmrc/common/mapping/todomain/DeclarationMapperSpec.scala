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
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}


class DeclarationMapperSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples {

  val output = DeclarationMapper.toDomain(desDeclaration,date, true)

  "Declaration Mapper" must {
    "Map fields correctly to a Domain declaration" when {
      "we have a correspondence address" in {
          output.correspondenceAddress.postalCode mustBe desDeclaration.address.postCode
      }

      "we have a firstName" in {
        output.givenName mustBe desDeclaration.name.firstName
      }

      "we have a middle name" in {
        output.otherName mustBe desDeclaration.name.middleName
      }

      "we have a family name" in {
        output.familyName mustBe desDeclaration.name.lastName
      }

      "we have a date" in {
        output.date mustBe date
      }

      "we have a confirmation" in {
        output.confirmation mustBe true
      }
    }
  }
}

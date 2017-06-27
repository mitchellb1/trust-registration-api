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

package uk.gov.hmrc.models.mapping

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.models.Declaration
import uk.gov.hmrc.utils.{JsonExamples, ScalaDataExamples}


class DeclarationMapperSpec extends PlaySpec with JsonExamples with ScalaDataExamples {

  "Declaration" must {
    "Convert to a valid DES Declaration JSON body" when {
      val declaration = trustWithEmploymentTrust.declaration
      val json: JsValue = Json.toJson(declaration)(Declaration.writesToDes)

      "we have a declaration with a first name" in {
        (json \  "name" \ "firstName").get.as[String] mustBe declaration.givenName
      }

      "we have a declaration with a last name" in {
        (json \  "name" \ "lastName").get.as[String] mustBe declaration.familyName
      }

      "there is declaration with middlename" in {
        (json \  "name" \ "middleName").get.as[String] mustBe declaration.otherName.get
      }

      "there is a declaration with no middlename" in {
        val declaration = trustWithEmploymentTrust.declaration.copy(otherName = None)
        val json: JsValue = Json.toJson(declaration)(Declaration.writesToDes)

        (json \  "name").toString() mustNot include("middleName")
      }

      "we have a declaration address" in {
        (json \  "address" \ "line1").get.as[String] mustBe declaration.correspondenceAddress.line1
      }
    }
  }

}

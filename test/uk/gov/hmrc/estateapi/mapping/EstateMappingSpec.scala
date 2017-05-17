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

package uk.gov.hmrc.estateapi.mapping


import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import uk.gov.hmrc.common.mapping.EstateMapper
import uk.gov.hmrc.common.utils.{DesSchemaValidator, SuccessfulValidation}
import uk.gov.hmrc.estateapi.rest.resources.core.EstateRequest
import uk.gov.hmrc.utils.ScalaDataExamples

class EstateMappingSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples {

  val domainEstateFromCaseClasses = EstateRequest(validEstateWithPersonalRepresentative)
  val domainEstateFromFileString: String = Json.prettyPrint(Json.toJson(validEstateWithPersonalRepresentative))

  val SUT = EstateMapper

  "EstateMapper" must {
    "accept a valid domain Estates case class" when {
      "and return a valid DesEstates case class" in {

        //println(s"From domain case classes ---- ${validEstateWithPersonalRepresentative}")
        println(s"From domain case classes ---- ${Json.toJson(validEstateWithPersonalRepresentative).toString()}}")
        val convertedJson = SUT.toDes(validEstateWithPersonalRepresentative)
        println(s"From des case classes ---- ${Json.toJson(convertedJson).toString()}")
        val result = DesSchemaValidator.validateAgainstSchema(Json.toJson(convertedJson).toString())
        result mustBe SuccessfulValidation
      }
    }

//    "accept a valid des Estates case class" when {
//      "and return a valid Domain Estates case class" in {
//
//        println(s"From case classes ---- ${validEstateWithPersonalRepresentative}")
//        val convertedJson = SUT.toDomain(completeValidDesEstate)
//        val result = DesSchemaValidator.validateAgainstSchema(Json.toJson(convertedJson).toString())
//        result mustBe SuccessfulValidation
//      }
//    }
  }
}

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
import uk.gov.hmrc.common.utils.{DesSchemaValidator, EstateSchemaValidator, SuccessfulValidation}
import uk.gov.hmrc.utils.{DesScalaExamples, ScalaDataExamples}

class EstateMappingSpec extends PlaySpec
  with OneAppPerSuite
  with ScalaDataExamples
  with DesScalaExamples{

  //  val domainEstateFromCaseClasses = EstateRequest(validEstateWithPersonalRepresentative)
  //  val domainEstateFromFileString: String = Json.prettyPrint(Json.toJson(validEstateWithPersonalRepresentative))

  val SUT = EstateMapper

  "EstateMapper" must {
    "accept a valid set of domain Estates case classes" when {
      "and return a valid set of DesEstates case classes" in {


        //println(s"From domain case classes ---- ${Json.toJson(validEstateWithPersonalRepresentative).toString()}}")
        val convertedToDesCaseClasses = SUT.toDes(validEstateWithPersonalRepresentative)
        println(s"From des case classes ---- ${Json.toJson(convertedToDesCaseClasses).toString()}")

        val result = DesSchemaValidator.validateAgainstSchema(Json.toJson(convertedToDesCaseClasses).toString())
        result mustBe SuccessfulValidation
      }
    }

    "accept a valid set of des Estates case class" when {
      "and return a set of valid Domain Estates case class" in {

        //println(s"From des case classes ---- ${completeValidDesEstate}")
        val convertedToDomainCaseClasses = SUT.toDomain(completeValidDesEstate)
        println(s"From domain case classes ---- ${convertedToDomainCaseClasses}")



        val result = EstateSchemaValidator.validateAgainstSchema(Json.toJson(convertedToDomainCaseClasses).toString())
        result mustBe SuccessfulValidation
      }
    }
  }
}

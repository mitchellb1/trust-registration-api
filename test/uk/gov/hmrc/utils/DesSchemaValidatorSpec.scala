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

package uk.gov.hmrc.utils

import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import uk.gov.hmrc.common.utils._

import scala.io.Source

class DesSchemaValidatorSpec extends PlaySpec
    with OneAppPerSuite
    with DesScalaExamples {

  "DesSchemaValidator" must {

    "read the schema and return a SuccessfulValidation for a estate json from file" when {
      "when we have a valid des json estate" in {
        lazy val desEstate = Source.fromFile(getClass.getResource("/des/desCompleteEstate.json").getPath).mkString
        //println(s"From file ---- ${desEstate}")
        val result = DesSchemaValidator.validateAgainstSchema(desEstate)
        result mustBe SuccessfulValidation
      }
    }

    "read the schema and return a SuccessfulValidation for a estate created from the case classes" when {
      "when we have a valid des json estate" in {
        val desEstate: String = Json.prettyPrint(Json.toJson(completeValidDesEstate))
        //println(s"From case classes ---- ${desEstate}")
        val result = DesSchemaValidator.validateAgainstSchema(desEstate)
        result mustBe SuccessfulValidation
      }
    }
    "read the schema and return a failedValidation for a estate from a file" when {
      "when we have an invalid des json estate" in {
        lazy val desEstateJsonNode = Source.fromFile(getClass.getResource("/des/desInvalidCompleteEstate.json").getPath).mkString
        val result = DesSchemaValidator.validateAgainstSchema(desEstateJsonNode.toString)
        result mustBe FailedValidation("Invalid Json",0,List(TrustsValidationError("""object has missing required properties (["line1","line2"])""","/correspondence/address")))
      }
    }

    "read the schema and return a SuccessfulValidation for a trust json from file" when {
      "when we have a valid des json trust" in {
        lazy val desTrust = Source.fromFile(getClass.getResource("/des/desCompleteTrust.json").getPath).mkString
        val result = DesSchemaValidator.validateAgainstSchema(desTrust)
        result mustBe SuccessfulValidation
      }
    }
  }
}

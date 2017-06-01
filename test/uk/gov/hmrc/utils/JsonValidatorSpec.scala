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

package uk.gov.hmrc.trustregistration.utils

import com.fasterxml.jackson.databind.JsonNode
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import uk.gov.hmrc.common.utils._
import uk.gov.hmrc.estateapi.rest.resources.core.EstateRequest
import uk.gov.hmrc.trustapi.rest.resources.core.TrustRequest
import uk.gov.hmrc.utils.{ScalaDataExamples, SchemaValidationExamples}

class SchemaValidatorSpec extends PlaySpec
    with OneAppPerSuite
    with ScalaDataExamples
    with SchemaValidationExamples {


  "TrustSchemaValidator" must {
    "read the schema and return a SuccessfulValidation for a valid json trust" when {
      "when we have a non required field missing" in {
        val jsonTrust = Json.toJson(TrustRequest(trustWithInterVivoTrust)).toString()
        //println(jsonTrust)
        val result = TrustSchemaValidator.validateAgainstSchema(jsonTrust)

        result mustBe SuccessfulValidation
      }
    }
  }

  "EstatesSchemaValidator" must {
    "read the schema and return a SuccessfulValidation for a valid json estate" when {
      "when we have a non required field missing" in {
        val jsonEstate = Json.toJson(EstateRequest(validEstateWithPersonalRepresentative)).toString()
        val result = EstateSchemaValidator.validateAgainstSchema(jsonEstate)

        result mustBe SuccessfulValidation
      }
    }
  }

  "JsonValidator" must {

    "read the schema and return a SuccessfulValidation" when {
      "when we have a non required field missing" in {
        val result = MultipleItemSchemaValidator.validateAgainstSchema(validJson)

        result mustBe SuccessfulValidation
      }

      "a field matches a specified pattern" in {
        val result = PostcodeSchemaValidator.validateAgainstSchema(validPostcodeJson)

        result mustBe SuccessfulValidation
      }
    }

    "read the schema and return a FailedValidation" when {
      "we miss a required field" in {
        val result = MultipleItemSchemaValidator.validateAgainstSchema(invalidJsonOneFieldMissing)

        result mustBe FailedValidation("Invalid Json", 0, List(TrustsValidationError("""object has missing required properties (["message"])""", "/")))
      }
      "we miss 2 required fields" in {
        val result = MultipleItemSchemaValidator.validateAgainstSchema(invalidJson)

        result mustBe FailedValidation("Invalid Json", 0, List(TrustsValidationError("""object has missing required properties (["location","message"])""", "/")))
      }
      "a field has the wrong type" in {
        val result = MultipleItemSchemaValidator.validateAgainstSchema(invalidTypeJson)

        result mustBe FailedValidation("Invalid Json", 0, List(TrustsValidationError("""instance type (integer) does not match any allowed primitive type (allowed: ["string"])""", "/code")))
      }
      "a field exceeds the maximum length" in {
        val result = MaxLengthSchemaValidator.validateAgainstSchema(invalidLengthJson)

        result mustBe FailedValidation("Invalid Json", 0, List(TrustsValidationError("""string "1234567890" is too long (length: 10, maximum allowed: 9)""", "/code")))
      }
      "a field doesn't match a specified pattern" in {
        val result = PostcodeSchemaValidator.validateAgainstSchema(invalidPostcodeJson)

        result mustBe FailedValidation("Invalid Json", 0, List(TrustsValidationError("""ECMA 262 regex "^[A-Za-z0-9]{3,4} [A-Za-z0-9]{3}$" does not match input string "NOT A POSTCODE"""", "/postalCode")))
      }
      "a required field is missing and one of the fields is the wrong type" in {
        val result = MultipleItemSchemaValidator.validateAgainstSchema(invalidJsonMultipleErrors)

        result mustBe FailedValidation("Invalid Json", 0, List(
          TrustsValidationError("""object has missing required properties (["code"])""", "/"),
          TrustsValidationError("""instance type (integer) does not match any allowed primitive type (allowed: ["string"])""", "/message")))
      }
      "a nested object has a missing field" in {
        val result = NestedItemSchemaValidator.validateAgainstSchema(invalidNestedJsonOneFieldMissing)

        result mustBe FailedValidation("Invalid Json", 0, List(TrustsValidationError("object has missing required properties ([\"message\"])", "/item")))
      }
      "we pass in some html rather than json" in {
        val result = MultipleItemSchemaValidator.validateAgainstSchema("<html></html>")

        result mustBe FailedValidation("Not JSON", 0, Nil)
      }
      "we pass duplicated elements" in {
        val result = MultipleItemSchemaValidator.validateAgainstSchema(duplicatedElementsJson)

        result mustBe FailedValidation("Duplicated Elements", 0, Nil)
      }
    }
  }


  object MultipleItemSchemaValidator extends JsonSchemaValidator {
    override val schema: JsonNode = multipleItemsSchema
  }

  object MaxLengthSchemaValidator extends JsonSchemaValidator {
    override val schema: JsonNode = maxLengthSchema
  }

  object PostcodeSchemaValidator extends JsonSchemaValidator {
    override val schema: JsonNode = postalCodeSchema
  }

  object NestedItemSchemaValidator extends JsonSchemaValidator {
    override val schema: JsonNode = nestedItemSchema
  }

  val duplicatedElementsJson: String =
    """
      {
        "message" : "test",
        "message" : "test"
      }
    """

  val invalidJsonMultipleErrors: String =
    """
      {
         "message" : 4444,
         "location" : "test"
      }
    """

  val validPostcodeJson: String =
    """
      {
         "postalCode" : "NE98 1ZZ"
      }
    """

  val invalidPostcodeJson: String =
    """
      {
         "postalCode" : "NOT A POSTCODE"
      }
    """

  val validJson: String =
    """
      |{
      |  "message" : "valid message",
      |  "code" : "valid code",
      |  "location" : "location"
      |}
    """.stripMargin

  val invalidJson: String =
    """
      |{
      |  "code" : "valid code"
      |}
    """.stripMargin

  val invalidJsonOneFieldMissing: String =
    """
      |{
      |  "location" : "test",
      |  "code" : "valid code"
      |}
    """.stripMargin

  val invalidNestedJsonOneFieldMissing: String =
    """
      {
        "item": {
          "code" : "12345"
        }
      }
    """

  val invalidTypeJson: String = """{"message" : "valid message", "code" : 5, "location":"this is a location"}"""

  val invalidLengthJson: String =
    """
      |{
      |  "code" : "1234567890"
      |}
    """.stripMargin

}

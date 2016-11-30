/*
 * Copyright 2016 HM Revenue & Customs
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

import com.fasterxml.jackson.core.{JsonFactory, JsonParser}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.report.LogLevel.ERROR
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

trait ValidationResult


case class TrustsValidationError(message: String, location: String)

case class FailedValidation(message: String, code: Int, validationErrors: Seq[TrustsValidationError]) extends ValidationResult

case class SuccessfulValidation() extends ValidationResult

object SuccessfulValidation extends ValidationResult

trait SchemaValidator{

  def validateAgainstSchema(schema: String, jsonNodeAsString: String): ValidationResult = {
    try {
      val objectMapper: ObjectMapper = new ObjectMapper()
          objectMapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION)

      val jsonFactory: JsonFactory = objectMapper.getFactory()
      val jsonParser: JsonParser = jsonFactory.createParser(jsonNodeAsString)

      objectMapper.readTree(jsonParser)

      val jsonAsNode: Try[JsonNode] = Try(JsonLoader.fromString(jsonNodeAsString))

      jsonAsNode match {
        case Success(json) => {
          val schemaNode: JsonNode = JsonLoader.fromString(schema)
          val factory: JsonSchema = JsonSchemaFactory.byDefault.getJsonSchema(schemaNode, "")
          val report: ProcessingReport = factory.validate(json, true)

          if (report.isSuccess) {
            SuccessfulValidation
          } else {
            val map: Seq[TrustsValidationError] = report.iterator.asScala.toList.filter(m => m.getLogLevel == ERROR).map(m => {
              val error = m.asJson()
              val message = error.findValue("message").asText("")
              val location = error.findValue("instance").at("/pointer").asText()

              TrustsValidationError(message, if (location == "") "/" else location)
            })

            FailedValidation("Invalid Json",0, map)
          }
        }
      }
    }
    catch {
      case ex: Exception => {
        println(ex.getMessage)
        if (ex.getMessage.contains("Duplicate")) {
          FailedValidation("Duplicated Elements", 0, Nil)
        } else {
          FailedValidation("Not JSON",0,Nil)
        }
      }
    }
  }
}

object SchemaValidator extends SchemaValidator

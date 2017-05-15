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

package uk.gov.hmrc.common.utils

import java.io.{PrintWriter, StringWriter}

import com.fasterxml.jackson.core.{JsonFactory, JsonParser}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.report.LogLevel.ERROR
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}
import play.api.{Logger, Play}

import scala.collection.JavaConverters._
import scala.util.{Success, Try}


trait JsonSchemaValidator {

  private val JsonErrorMessageTag = "message"
  private val JsonErrorInstanceTag = "instance"
  private val JsonErrorPointerTag = "pointer"


  val schema: JsonNode

  def validateAgainstSchema(input: String): ValidationResult = {

    try {
      val jsonToValidate: Try[JsonNode] = doNotAllowDuplicatedProperties(input)

      jsonToValidate match {
        case Success(json) => {
          val validator: JsonSchema = JsonSchemaFactory.byDefault.getJsonSchema(schema)
          val validationOutput: ProcessingReport = validator.validate(json, true)

          if (validationOutput.isSuccess) {
            SuccessfulValidation
          } else {
            val failedValidation = FailedValidation("Invalid Json",0, getValidationErrors(validationOutput))
            Logger.info("Failed schema validation")
            Logger.debug(failedValidation.toString)
            failedValidation
          }
        }
      }
    }
    catch {
      case ex: Exception => {

        Logger.debug(s"Error validating Json request against Schema: ${ex.getMessage}")

        // log stack trace
        val sw = new StringWriter
        ex.printStackTrace(new PrintWriter(sw))
        Logger.debug(sw.toString)
        // ---------------

        if (ex.getMessage.contains("Duplicate")) {
          FailedValidation("Duplicated Elements", 0, Nil)
        } else {
          FailedValidation("Not JSON",0,Nil)
        }
      }
    }
  }


  private def getValidationErrors(validationOutput: ProcessingReport): Seq[TrustsValidationError] = {
    val validationErrors: Seq[TrustsValidationError] = validationOutput.iterator.asScala.toList.filter(m => m.getLogLevel == ERROR).map(m => {
      val error = m.asJson()
      val message = error.findValue(JsonErrorMessageTag).asText("")
      val location = error.findValue(JsonErrorInstanceTag).at(s"/$JsonErrorPointerTag").asText()

      TrustsValidationError(message, if (location == "") "/" else location)
    })
    validationErrors
  }

  private def doNotAllowDuplicatedProperties(jsonNodeAsString: String): Try[JsonNode] = {
    val objectMapper: ObjectMapper = new ObjectMapper()
    objectMapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION)

    val jsonFactory: JsonFactory = objectMapper.getFactory()
    val jsonParser: JsonParser = jsonFactory.createParser(jsonNodeAsString)

    objectMapper.readTree(jsonParser)

    val jsonAsNode: Try[JsonNode] = Try(JsonLoader.fromString(jsonNodeAsString))
    jsonAsNode
  }
}

object TrustSchemaValidator extends JsonSchemaValidator {
  import play.api.Play.current

  lazy val schema: JsonNode = JsonLoader.fromPath(s"${Play.application.path}/public/api/trusts/conf/1.0/schemas/trustsSchema.json")
}

object EstateSchemaValidator extends JsonSchemaValidator {
  import play.api.Play.current

  lazy val schema: JsonNode = JsonLoader.fromPath(s"${Play.application.path}/public/api/estates/conf/1.0/schemas/estatesSchema.json")
}

object DesSchemaValidator extends JsonSchemaValidator {
  import play.api.Play.current

  lazy val schema: JsonNode = JsonLoader.fromPath(s"${Play.application.path}/conf/desSchema.json")
}

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
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

trait ValidationResult

case class FailedValidation(errors: Seq[String]) extends ValidationResult

case class SuccessfulValidation() extends ValidationResult

object SuccessfulValidation extends ValidationResult

trait JsonSchemaValidator {
  val schemaFilename: String

  def validateAgainstSchema(jsonNodeAsString: String, schemaNodeAsString: String): ValidationResult = {
    try {
      val objectMapper: ObjectMapper = new ObjectMapper()
      objectMapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION)

      val jsonFactory: JsonFactory = objectMapper.getFactory()
      val jsonParser: JsonParser = jsonFactory.createParser(jsonNodeAsString)
      objectMapper.readTree(jsonParser) //Throws exception here if duplicate elements not inside an array

      val jsonAsNode: Try[JsonNode] = Try(JsonLoader.fromString(jsonNodeAsString))
      jsonAsNode match {
        case Failure(ex) => {
          println(ex.getMessage)
          FailedValidation(Seq(ex.getMessage))
        }
        case Success(json) => {
          val schema: JsonNode = JsonLoader.fromResource(s"/public/api/conf/$schemaFilename")
          val factory: JsonSchema = JsonSchemaFactory.byDefault.getJsonSchema(schema, schemaNodeAsString)
          val report: ProcessingReport = factory.validate(json)

          if (report.isSuccess) {
            println(s"report => $report")
            SuccessfulValidation
          } else {
            println(s"report => $report")
            //TODO : Parse json and add in "code" to convert output to comply with the json error schema
            //TODO : Maybe validate output to schema???????
            FailedValidation(report.iterator().asScala.toSeq.map(pm => pm.asJson().toString))
          }
        }
      }
    }
    catch {
      case ex: Exception => {
        println(ex.getMessage)
        //TODO : Check what other types of error message can occur here
        if (ex.getMessage.contains("Duplicate")) {
          FailedValidation(Seq("""{"message": "Duplicate elements","code": "400"}"""))
        } else {
          FailedValidation(Seq(ex.getMessage))
        }
      }
    }
  }
}

object JsonSchemaValidator extends JsonSchemaValidator {
  val schemaFilename = "1.0/schemas/trusts.json"
}
